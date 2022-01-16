package note.webnote.web.form;

import lombok.Getter;
import lombok.Setter;
import note.webnote.domain.Permission;

@Getter @Setter
public class AddParticipantForm {
    private Long loginId;
    private Long noteId;

    private String memberName;
    private PermissionNotHostEnum permission;

    public AddParticipantForm(Long loginId, Long noteId) {
        this.loginId = loginId;
        this.noteId = noteId;
    }
}
