package note.webnote.web.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import note.webnote.domain.Note;

@Getter @Setter
public class NoteSaveForm {

    private Long memberId;
    private Long noteId;

    private String title;
    private String content;

    public NoteSaveForm() {
    }

    public NoteSaveForm(Note note, Long memberId) {
        this.memberId = memberId;
        noteId = note.getId();
        title = note.getTitle();
        content = note.getContent();

    }
}
