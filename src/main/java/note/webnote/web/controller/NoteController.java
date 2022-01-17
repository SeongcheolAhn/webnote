package note.webnote.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import note.webnote.domain.Note;
import note.webnote.web.dto.EditNoteDto;
import note.webnote.web.dto.EditPermissionDto;
import note.webnote.web.dto.ParticipantsDto;
import note.webnote.web.dto.ViewNoteDto;
import note.webnote.web.form.AddParticipantForm;
import note.webnote.web.form.EditNoteForm;
import note.webnote.web.form.NoteSaveForm;
import note.webnote.web.service.LoginService;
import note.webnote.web.service.NoteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("{loginMemberId}/notes")
public class NoteController {

    private final NoteService noteService;
    private final LoginService loginService;

    @GetMapping("/{noteId}")
    public String note(@PathVariable Long noteId,
                       @PathVariable Long loginMemberId,
                       Model model) {
        log.info("[GET]    findNote");
        ViewNoteDto dto = noteService.findNote(noteId, loginMemberId);
        model.addAttribute("viewNoteDto", dto);
        return "notes/viewNote";
    }

    @GetMapping("/new")
    public String createNoteForm(@PathVariable Long loginMemberId, Model model) {
        log.info("[GET]    createNoteForm");
        NoteSaveForm noteSaveForm = new NoteSaveForm();
        noteSaveForm.setMemberId(loginMemberId);
        model.addAttribute("noteSaveForm", noteSaveForm);
        return "notes/addNoteForm";
    }

    @PostMapping("/new")
    public String createNote(@ModelAttribute NoteSaveForm form,
                             @PathVariable Long loginMemberId) {
        log.info("[POST]    createNote");
        Long noteId = noteService.saveNoteAndMemberNote(form, loginMemberId);
        String redirectUrl = "/" + loginMemberId + "/notes/" + noteId;
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/{noteId}/edit")
    public String editNoteForm(@PathVariable Long noteId,
                               @PathVariable Long loginMemberId,
                               Model model, HttpServletRequest request) {
        log.info("[GET]    editNoteForm");

        Optional<Note> findNote = noteService.findOne(noteId);
        if (findNote.isEmpty()) {
            log.info("노트 존재하지 않음");
            return "{memberId}/notes/{noteId}";
        }
        Note note = findNote.get();


        model.addAttribute("editNoteForm", new EditNoteForm(note));
        model.addAttribute("editNoteDto", new EditNoteDto(note, loginMemberId, noteService.findHost(note, loginMemberId)));
        return "notes/editNoteForm";
    }

    @PostMapping("/{noteId}/edit")
    public String editNote(@ModelAttribute EditNoteForm editNoteForm,
                           @PathVariable Long loginMemberId,
                           @PathVariable Long noteId) {
        log.info("[POST]    editNote");

        noteService.saveEditNote(noteId, editNoteForm);

        String redirectUrl = "/" + loginMemberId + "/notes/" + noteId;
        return "redirect:" + redirectUrl;
    }

    /**
     * 특정 멤버의 권한 변경
     * HOST 권한을 가진 멤버만 접근 가능
     */
    @GetMapping("/{noteId}/edit/{editMemberId}")
    public String editPermissionForm(@PathVariable Long editMemberId,
                                     @PathVariable Long noteId,
                                     Model model) {
        log.info("[GET]    editPermissionForm");
        String[] permissions = {"READ_WRITE", "READ_ONLY"};

        model.addAttribute("editPermissionDto", noteService.findEditPermissionDto(editMemberId, noteId));
        model.addAttribute("permissions", permissions);
        return "notes/editPermissionForm";
    }

    @PostMapping("/{noteId}/edit/{editMemberId}")
    public String editPermission(@PathVariable Long loginMemberId,
                                 @PathVariable Long editMemberId,
                                 @PathVariable Long noteId,
                                 @ModelAttribute EditPermissionDto editPermissionDto) {
        log.info("[POST]    editPermission");

        noteService.editPermission(noteId, editMemberId, editPermissionDto);

        String redirectURL ="/" + loginMemberId + "/notes/" + noteId;
        return "redirect:" + redirectURL;
    }

    /**
     * 참여자 수정 페이지
     */
    @GetMapping("/{noteId}/edit/participants")
    public String editParticipants(@PathVariable Long loginMemberId,
                                   @PathVariable Long noteId,
                                   Model model, HttpServletRequest request) {
        // 권한 체크
        Long findLoginId = loginService.checkMember(request);
        if(!Objects.equals(findLoginId, loginMemberId)) {
            return "home";
        }


        ParticipantsDto participants = noteService.participants(noteId, loginMemberId);
        model.addAttribute("participantsDto", participants);
        return "notes/participants";
    }

    /**
     * 참여자 삭제
     */
    @GetMapping("/{noteId}/edit/delete/{deleteMemberId}")
    public String deleteMember(@PathVariable Long loginMemberId,
                               @PathVariable Long noteId,
                               @PathVariable Long deleteMemberId,
                               HttpServletRequest request) {
        noteService.deleteMember(loginMemberId, noteId, deleteMemberId, request);
        String redirectURL = "/" + loginMemberId + "/notes/" + noteId;
        return "redirect:" + redirectURL;
    }

    /**
     * 참여자 추가
     */
    @GetMapping("/{noteId}/edit/participants/new")
    public String addParticipantForm(@PathVariable Long loginMemberId,
                                 @PathVariable Long noteId,
                                 HttpServletRequest request, Model model) {

        // 정상 회원 확인
        if (!loginService.check(request, loginMemberId)) {
            return "home";
        }

        AddParticipantForm addParticipantForm = new AddParticipantForm(loginMemberId, noteId);
        model.addAttribute("addParticipantForm", addParticipantForm);
        return "notes/addParticipant";
    }

    @PostMapping("/{noteId}/edit/participants/new")
    public String addParticipant(@PathVariable Long loginMemberId,
                                 @PathVariable Long noteId,
                                 @ModelAttribute AddParticipantForm addParticipantForm,
                                 BindingResult bindingResult,
                                 HttpServletRequest request) {

        // 정상 회원 확인
        if (!loginService.check(request, loginMemberId)) {
            return "home";
        }

        String errorMessage = noteService.addParticipant(loginMemberId, noteId, addParticipantForm);
        if (!errorMessage.equals("success")) {
            bindingResult.reject("addFail", errorMessage);
            return "notes/addParticipant";
        }

        String redirectURL = "/" + loginMemberId + "/notes/" + noteId;
        return "redirect:" + redirectURL;
    }
}
