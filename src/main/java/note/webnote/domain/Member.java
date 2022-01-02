package note.webnote.domain;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
public class Member {

    @Id @GeneratedValue
    private Long id;
    private String loginId;
    private String password;
    private String name;
    private LocalDateTime createdDate;

    public Member(String loginId, String password, String name) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.createdDate = LocalDateTime.now();
    }
}
