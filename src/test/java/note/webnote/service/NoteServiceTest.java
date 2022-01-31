package note.webnote.service;

import note.webnote.domain.Member;
import note.webnote.domain.MemberNote;
import note.webnote.domain.Note;
import note.webnote.domain.Permission;
import note.webnote.repository.NoteRepository;
import note.webnote.web.service.MemberService;
import note.webnote.web.service.NoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Fail.fail;

@SpringBootTest
@Transactional
public class NoteServiceTest {

    @Autowired
    NoteService noteService;

    @Autowired
    MemberService memberService;

    @Autowired
    NoteRepository noteRepository;

    @Test
    void 노트삭제() {
        // given
        Note note = new Note("testNoteA", "test content A");
        Long noteId = noteService.saveNote(note);

        Member member = new Member("testMemberA", "testLoginA", "1234");
        memberService.join(member);

        noteRepository.saveMemberNote(new MemberNote(member, note, Permission.HOST));

        // when
        noteService.deleteNote(note.getId());

        // then
        Optional<Note> result = noteService.findOne(noteId);
        if (result.isPresent()) {
            fail("노트 삭제 테스트 실패");
        }
    }
}
