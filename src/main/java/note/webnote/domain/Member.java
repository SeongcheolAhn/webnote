package note.webnote.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue
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
