package note.webnote.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    private String loginId;
    private String password;
    private String name;
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "member")
    private List<MemberNote> memberNotes = new ArrayList<>();

    public Member(String name, String loginId, String password) {
        this.name = name;
        this.loginId = loginId;
        this.password = password;
        this.createdDate = LocalDateTime.now();
    }

    public void setEncodedPassword(String encodedPassword) {
        password = encodedPassword;
    }
}
