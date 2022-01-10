package note.webnote.web.form;

import lombok.Getter;
import lombok.Setter;
import note.webnote.domain.Note;
import note.webnote.domain.Permission;
import note.webnote.web.dto.ViewNoteParticipantDto;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
public class EditNoteForm {

    private String title;
    private String content;
    private List<ViewNoteParticipantDto> participants;

    public EditNoteForm() {
    }

    public EditNoteForm(Note note) {
        title = note.getTitle();
        content = note.getContent();
        participants = note.getMemberNote().stream()
                .map(ViewNoteParticipantDto::new)
                .filter(p -> p.getPermission() != Permission.HOST)
                .collect(Collectors.toList());
    }
}
