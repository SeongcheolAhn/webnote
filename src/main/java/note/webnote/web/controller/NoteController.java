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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("{loginId}/notes")
public class NoteController {

    private final NoteService noteService;
    private final LoginService loginService;

    @GetMapping("/{noteId}")
    public String note(@PathVariable Long noteId,
                       @PathVariable Long loginId,
                       Model model) {
        log.info("[GET]    findNote");
        ViewNoteDto dto = noteService.findNote(noteId, loginId);
        model.addAttribute("viewNoteDto", dto);
        return "notes/viewNote";
    }

    @GetMapping("/new")
    public String createNoteForm(@PathVariable Long loginId, Model model) {
        log.info("[GET]    createNoteForm");
        NoteSaveForm noteSaveForm = new NoteSaveForm();
        noteSaveForm.setMemberId(loginId);
        model.addAttribute("noteSaveForm", noteSaveForm);
        return "notes/addNoteForm";
    }

    @PostMapping("/new")
    public String createNote(@ModelAttribute NoteSaveForm form,
                             @PathVariable Long loginId) {
        log.info("[POST]    createNote");
        Long noteId = noteService.saveMemberNoteWithNote(form, loginId);
        String redirectUrl = "/" + loginId + "/notes/" + noteId;
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/{noteId}/edit")
    public String editNoteForm(@PathVariable Long noteId,
                               @PathVariable Long loginId,
                               Model model, HttpServletRequest request) {
        log.info("[GET]    editNoteForm");

        Optional<Note> findNote = noteService.findOne(noteId);
        if (findNote.isEmpty()) {
            log.info("노트 존재하지 않음");
            return "{memberId}/notes/{noteId}";
        }
        Note note = findNote.get();


        model.addAttribute("editNoteForm", new EditNoteForm(note));
        model.addAttribute("editNoteDto", new EditNoteDto(note, loginId, noteService.findHost(note, loginId)));
        return "notes/editNoteForm";
    }

    @PostMapping("/{noteId}/edit")
    public String editNote(@ModelAttribute EditNoteForm editNoteForm,
                           @PathVariable Long loginId,
                           @PathVariable Long noteId) {
        log.info("[POST]    editNote");

        noteService.saveEditNote(noteId, editNoteForm);

        String redirectUrl = "/" + loginId + "/notes/" + noteId;
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
    public String editPermission(@PathVariable Long loginId,
                                 @PathVariable Long editMemberId,
                                 @PathVariable Long noteId,
                                 @ModelAttribute EditPermissionDto editPermissionDto) {
        log.info("[POST]    editPermission");

        noteService.editPermission(noteId, editMemberId, editPermissionDto);

        String redirectURL ="/" + loginId + "/notes/" + noteId;
        return "redirect:" + redirectURL;
    }

    /**
     * 참여자 수정 페이지
     */
    @GetMapping("/{noteId}/edit/participants")
    public String editParticipants(@PathVariable Long loginId,
                                   @PathVariable Long noteId,
                                   Model model, HttpServletRequest request) {
        // 권한 체크
        Long findLoginId = loginService.checkMember(request);
        if(!Objects.equals(findLoginId, loginId)) {
            return "home";
        }


        ParticipantsDto participants = noteService.participants(noteId, loginId);
        model.addAttribute("participantsDto", participants);
        return "notes/participants";
    }

    /**
     * 참여자 삭제
     */
    @GetMapping("/{noteId}/edit/delete/{deleteMemberId}")
    public String deleteMember(@PathVariable Long loginId,
                               @PathVariable Long noteId,
                               @PathVariable Long deleteMemberId,
                               HttpServletRequest request) {
        noteService.deleteMember(loginId, noteId, deleteMemberId, request);
        String redirectURL = "/" + loginId + "/notes/" + noteId;
        return "redirect:" + redirectURL;
    }

    /**
     * 참여자 추가
     */
    @GetMapping("/{noteId}/edit/participants/new")
    public String addParticipantForm(@PathVariable Long loginId,
                                 @PathVariable Long noteId,
                                 HttpServletRequest request, Model model) {

        // 정상 회원 확인
        if (!loginService.check(request, loginId)) {
            return "home";
        }

        AddParticipantForm addParticipantForm = new AddParticipantForm(loginId, noteId);
        model.addAttribute("addParticipantForm", addParticipantForm);
        return "notes/addParticipant";
    }

    @PostMapping("/{noteId}/edit/participants/new")
    public String addParticipant(@PathVariable Long loginId,
                                 @PathVariable Long noteId,
                                 @ModelAttribute AddParticipantForm addParticipantForm,
                                 HttpServletRequest request) {

        // 정상 회원 확인
        if (!loginService.check(request, loginId)) {
            return "home";
        }

        noteService.addParticipant(loginId, noteId, addParticipantForm);

        String redirectURL = "/" + loginId + "/notes/" + noteId;
        return "redirect:" + redirectURL;
    }
}
