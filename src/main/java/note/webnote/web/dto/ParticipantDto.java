package note.webnote.web.dto;

import lombok.Getter;
import lombok.Setter;
import note.webnote.domain.Permission;

@Getter @Setter
public class ParticipantDto {
    private Long id;
    private String name;
    private Permission permission;

    public ParticipantDto(Long id, String name, Permission permission) {
        this.id = id;
        this.name = name;
        this.permission = permission;
    }

    public ParticipantDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ParticipantDto() {
    }
}
