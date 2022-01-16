package note.webnote.repository;

import note.webnote.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long>, MemberNoteRepository {

}
