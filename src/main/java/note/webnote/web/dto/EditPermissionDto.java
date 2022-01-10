package note.webnote.web.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 회원 권한 변경 페이지에서 사용
 */
@Getter @Setter
public class EditPermissionDto {
    private Long editMemberId;
    private String editMemberName;
    private String editPermission;

    public EditPermissionDto(Long editMemberId, String editMemberName, String editPermission) {
        this.editMemberId = editMemberId;
        this.editMemberName = editMemberName;
        this.editPermission = editPermission;
    }
}
