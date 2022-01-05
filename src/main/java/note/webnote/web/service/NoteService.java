package note.webnote.web.service;

import lombok.RequiredArgsConstructor;
import note.webnote.domain.Note;
import note.webnote.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    public Long saveNote(Note note) {
        noteRepository.save(note);
        return note.getId();
    }

    public Optional<Note> findOne(Long id) {
        return noteRepository.findById(id);
    }
}
