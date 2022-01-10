package note.webnote.web.dto;

import lombok.Getter;
import lombok.Setter;
import note.webnote.domain.Note;

/**
 *
 */
@Getter @Setter
public class EditNoteDto {

    private Long noteId;
    private Long loginId;
    private boolean isHost;

    public EditNoteDto(Note note, Long loginId, Long hostId) {
        noteId = note.getId();
        this.loginId = loginId;
        isHost = loginId.equals(hostId);
    }
}
