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
public class Note {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_id")
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

    public void editLastModifiedDate(LocalDateTime editDateTime) {
        this.lastModifiedDate = editDateTime;
    }
}
