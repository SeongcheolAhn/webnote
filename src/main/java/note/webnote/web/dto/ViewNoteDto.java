package note.webnote.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ViewNoteDto {

    private String title;
    private String content;
    private String hostMemberName;

    public ViewNoteDto(String title, String content, String hostMemberName) {
        this.title = title;
        this.content = content;
        this.hostMemberName = hostMemberName;
    }
}
