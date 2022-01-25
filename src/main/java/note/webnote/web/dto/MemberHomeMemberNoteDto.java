package note.webnote.web.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import note.webnote.domain.MemberNote;
import note.webnote.domain.Permission;

import java.time.LocalDateTime;

@Getter
public class MemberHomeMemberNoteDto {

    private Long noteId;
    private String title;
    private LocalDateTime lastModifiedDate;
    private Permission permission;

    public MemberHomeMemberNoteDto(MemberNote memberNote) {
        this.noteId = memberNote.getNote().getId();
        this.title = memberNote.getNote().getTitle();
        this.lastModifiedDate = memberNote.getNote().getLastModifiedDate();
        this.permission = memberNote.getPermission();
    }

    @QueryProjection
    public MemberHomeMemberNoteDto(Long noteId, String title, LocalDateTime lastModifiedDate, Permission permission) {
        this.noteId = noteId;
        this.title = title;
        this.lastModifiedDate = lastModifiedDate;
        this.permission = permission;
    }
}
