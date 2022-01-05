package note.webnote.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ViewNoteDto {

    private String title;
    private String content;
    private String hostMemberName;
    private Long hostMemberId;
    private Long noteId;

    public ViewNoteDto(String title, String content, String hostMemberName, Long hostMemberId, Long noteId) {
        this.title = title;
        this.content = content;
        this.hostMemberName = hostMemberName;
        this.hostMemberId = hostMemberId;
        this.noteId = noteId;
    }
}
