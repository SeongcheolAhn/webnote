package note.webnote.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import note.webnote.domain.MemberNote;
import note.webnote.domain.Note;
import note.webnote.repository.NoteRepository;
import note.webnote.web.dto.ViewNoteDto;
import note.webnote.web.form.NoteSaveForm;
import note.webnote.web.service.NoteService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
                               Model model) {
        log.info("[GET]    editNoteForm");

        Optional<Note> note = noteService.findOne(noteId);

        if (note.isEmpty()) {
            log.info("노트 존재하지 않음");
            return "{memberId}/notes/{noteId}";
        }

        model.addAttribute("noteSaveForm", new NoteSaveForm(note.get(), memberId));
        return "notes/editNoteForm";
    }

    @PostMapping("/{noteId}/edit")
    public String editNote(@ModelAttribute NoteSaveForm noteSaveForm,
                           @PathVariable Long memberId,
                           @PathVariable Long noteId) {
        log.info("[POST]    editNote");

        noteService.saveEditNote(noteId, noteSaveForm);

        String redirectUrl = "/" + memberId + "/notes/" + noteId;
        return "redirect:" + redirectUrl;
    }
}
