package note.webnote.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class MemberNote {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_note_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id")
    private Note note;

    @Enumerated(value = EnumType.STRING)
    private Permission permission;

    public MemberNote(Member member, Note note, Permission permission) {
        this.member = member;
        this.note = note;
        this.permission = permission;

        // 양방향 저장
        note.getMemberNote().add(this);
        member.getMemberNotes().add(this);
    }

    public void editPermission(Permission permission) {
        this.permission = permission;
    }
}
