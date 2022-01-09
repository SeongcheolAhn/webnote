package note.webnote.web.dto;

import lombok.Getter;
import lombok.Setter;
import note.webnote.domain.MemberNote;
import note.webnote.domain.Permission;

@Getter @Setter
public class ViewNoteParticipantDto {

    private Long memberId;
    private String memberName;
    private Permission permission;

    public ViewNoteParticipantDto(MemberNote memberNote) {
        memberId = memberNote.getMember().getId();
        memberName = memberNote.getMember().getName();
        permission = memberNote.getPermission();
    }
}
