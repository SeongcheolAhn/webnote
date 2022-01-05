package note.webnote.web.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import note.webnote.domain.Member;
import note.webnote.domain.MemberNote;
import note.webnote.domain.Note;
import note.webnote.domain.Permission;
import note.webnote.repository.MemberRepository;
import note.webnote.repository.NoteRepository;
import note.webnote.web.dto.ViewNoteDto;
import note.webnote.web.form.NoteSaveForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoteService {

    private final NoteRepository noteRepository;
    private final MemberRepository memberRepository;

    public Long saveNote(Note note) {
        noteRepository.save(note);
        return note.getId();
    }

    public Optional<Note> findOne(Long id) {
        return noteRepository.findById(id);
    }

    /* Note 엔티티와 MemberNote 모두 저장 */
    @Transactional
    public Long saveMemberNoteWithNote(NoteSaveForm form) {
        Note note = new Note(form.getTitle(), form.getContent());
        Long noteId = saveNote(note);

        // form 저장 마무리
        form.setNoteId(noteId);

        // MemberNote 저장
        Optional<Member> member = memberRepository.findById(form.getMemberId());
        if (member.isEmpty()) {
            log.info("존재하지 않는 회원입니다.");
            return null;
        }

        MemberNote memberNote = new MemberNote(member.get(), note, Permission.HOST);
        noteRepository.saveMemberNote(memberNote);
        return noteId;
    }


    /**
     *
     */
    public ViewNoteDto findNote(Long noteId) {
        Optional<Note> findNote = noteRepository.findById(noteId);
        if(findNote.isEmpty()) {
            return null;
        }

        Note note = findNote.get();
        Member member = note.getMemberNote().stream()
                .filter(mn -> mn.getPermission() == Permission.HOST)
                .findFirst().get().getMember();


        return new ViewNoteDto(note.getTitle(), note.getContent(), member.getName());
    }
}
