package note.webnote.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import note.webnote.domain.MemberNote;
import note.webnote.domain.Note;
import note.webnote.domain.Permission;
import note.webnote.repository.NoteRepository;
import note.webnote.web.dto.EditNoteDto;
import note.webnote.web.dto.EditPermissionDto;
import note.webnote.web.dto.ViewNoteDto;
import note.webnote.web.dto.ViewNoteParticipantDto;
import note.webnote.web.form.EditNoteForm;
import note.webnote.web.form.NoteSaveForm;
import note.webnote.web.intercptor.SessionMember;
import note.webnote.web.service.NoteService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("{memberId}/notes")
public class NoteController {

    private final NoteService noteService;

    @GetMapping("/{noteId}")
    public String note(@PathVariable Long noteId,
                       @PathVariable Long memberId,
                       Model model) {
        log.info("[GET]    findNote");
        ViewNoteDto dto = noteService.findNote(noteId, memberId);
        model.addAttribute("viewNoteDto", dto);
        return "notes/viewNote";
    }

    @GetMapping("/new")
    public String createNoteForm(@PathVariable Long memberId, Model model) {
        log.info("[GET]    createNoteForm");
        NoteSaveForm noteSaveForm = new NoteSaveForm();
        noteSaveForm.setMemberId(memberId);
        model.addAttribute("noteSaveForm", noteSaveForm);
        return "notes/addNoteForm";
    }

    @PostMapping("/new")
    public String createNote(@ModelAttribute NoteSaveForm form,
                             @PathVariable Long memberId) {
        log.info("[POST]    createNote");
        Long noteId = noteService.saveMemberNoteWithNote(form);
        String redirectUrl = "/" + memberId + "/notes/" + noteId;
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/{noteId}/edit")
    public String editNoteForm(@PathVariable Long noteId,
                               @PathVariable Long memberId,
                               Model model, HttpServletRequest request) {
        log.info("[GET]    editNoteForm");

        Optional<Note> findNote = noteService.findOne(noteId);
        if (findNote.isEmpty()) {
            log.info("노트 존재하지 않음");
            return "{memberId}/notes/{noteId}";
        }
        Note note = findNote.get();


        model.addAttribute("editNoteForm", new EditNoteForm(note));
        model.addAttribute("editNoteDto", new EditNoteDto(note, memberId, noteService.findHost(note, memberId)));
        return "notes/editNoteForm";
    }

    @PostMapping("/{noteId}/edit")
    public String editNote(@ModelAttribute EditNoteForm editNoteForm,
                           @PathVariable Long memberId,
                           @PathVariable Long noteId) {
        log.info("[POST]    editNote");

        noteService.saveEditNote(noteId, editNoteForm);

        String redirectUrl = "/" + memberId + "/notes/" + noteId;
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
    public String editPermission(@PathVariable Long memberId,
                                 @PathVariable Long editMemberId,
                                 @PathVariable Long noteId,
                                 @ModelAttribute EditPermissionDto editPermissionDto) {
        log.info("[POST]    editPermission");

        noteService.editPermission(noteId, editMemberId, editPermissionDto);

        String redirectURL ="/" + memberId + "/notes/" + noteId;
        return "redirect:" + redirectURL;
    }
}
