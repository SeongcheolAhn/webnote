package note.webnote.web.dto;

import lombok.Getter;
import lombok.Setter;
import note.webnote.domain.MemberNote;
import note.webnote.domain.Permission;

import java.time.LocalDateTime;

@Getter @Setter
public class ViewMemberNoteDto {

    private Long memberId;
    private Long noteId;
    private Permission permission;

    private String title;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public ViewMemberNoteDto(MemberNote memberNote) {
        this.memberId = memberNote.getMember().getId();
        this.noteId = memberNote.getNote().getId();
        this.permission = memberNote.getPermission();
        this.title = memberNote.getNote().getTitle();
        this.content = memberNote.getNote().getContent();
        this.createdDate = memberNote.getNote().getCreatedDate();
        this.lastModifiedDate = memberNote.getNote().getLastModifiedDate();
    }
}
