package note.webnote.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class MemberNote {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NOTE_ID")
    private Note note;

    @Enumerated(value = EnumType.STRING)
    private Permission permission;

    public MemberNote(Member member, Note note, Permission permission) {
        this.member = member;
        this.note = note;
        this.permission = permission;
    }

    public void editPermission(Permission permission) {
        this.permission = permission;
    }
}
