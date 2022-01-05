package note.webnote.web.form;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NoteSaveForm {

    private Long memberId;
    private Long noteId;

    private String title;
    private String content;
}
