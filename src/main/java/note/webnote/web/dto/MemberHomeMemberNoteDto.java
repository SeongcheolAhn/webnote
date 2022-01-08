package note.webnote.web.dto;

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
}
