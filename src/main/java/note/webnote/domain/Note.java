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
public class Note {

    @Id @GeneratedValue
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    @OneToMany(mappedBy = "note")
    private List<MemberNote> memberNote = new ArrayList<>();

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
        this.createdDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
    }

    public void editTitle(String title) {
        this.title = title;
    }

    public void editContent(String content) {
        this.content = content;
    }
}
