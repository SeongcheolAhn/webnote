package note.webnote.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import note.webnote.domain.Note;
import note.webnote.web.dto.*;
import note.webnote.web.form.AddParticipantForm;
import note.webnote.web.form.EditNoteForm;
import note.webnote.web.form.NoteSaveForm;
import note.webnote.web.service.LoginService;
import note.webnote.web.service.MemberService;
import note.webnote.web.service.NoteService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    private final MemberService memberService;

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
            log.info("?????? ???????????? ??????");
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
     * ?????? ????????? ?????? ??????
     * HOST ????????? ?????? ????????? ?????? ??????
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
     * ????????? ?????? ?????????
     */
    @GetMapping("/{noteId}/edit/participants")
    public String editParticipants(@PathVariable Long loginMemberId,
                                   @PathVariable Long noteId,
                                   Model model, HttpServletRequest request) {
        // ?????? ??????
        Long findLoginId = loginService.checkMember(request);
        if(!Objects.equals(findLoginId, loginMemberId)) {
            return "home";
        }


        ParticipantsDto participants = noteService.participants(noteId, loginMemberId);
        model.addAttribute("participantsDto", participants);
        return "notes/participants";
    }

    /**
     * ????????? ??????
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
     * ????????? ??????
     */
    @GetMapping("/{noteId}/edit/participants/new")
    public String addParticipantForm(@PathVariable Long loginMemberId,
                                 @PathVariable Long noteId,
                                 HttpServletRequest request, Model model) {

        // ?????? ?????? ??????
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

        // ?????? ?????? ??????
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

    /**
     * ?????? ??????
     */
    @GetMapping("/{noteId}/delete")
    public String deleteNote(@PathVariable Long noteId,
                             @PathVariable Long loginMemberId,
                             MemberHomeCondition condition,
                             @PageableDefault(size = 5) Pageable pageable,
                             Model model, HttpServletRequest request) {
        log.info("[GET] ?????? ??????");

        noteService.deleteNote(noteId);
        memberService.findNotes(loginMemberId, condition, pageable, model);

        String redirectURL = "/members/" + loginMemberId;

        return "redirect:" + redirectURL;
    }
}
