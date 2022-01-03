package note.webnote.repository;

import lombok.RequiredArgsConstructor;
import note.webnote.domain.Note;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NoteRepository {

    private final EntityManager em;

    public void save(Note note) {
        em.persist(note);
    }

    public Optional<Note> findById(Long id) {
        return Optional.ofNullable(em.find(Note.class, id));
    }
}
