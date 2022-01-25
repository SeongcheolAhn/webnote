package note.webnote;

import lombok.RequiredArgsConstructor;
import note.webnote.domain.Member;
import note.webnote.domain.MemberNote;
import note.webnote.domain.Note;
import note.webnote.domain.Permission;
import note.webnote.repository.MemberNoteRepository;
import note.webnote.repository.NoteRepository;
import note.webnote.web.service.MemberService;
import note.webnote.web.service.NoteService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

//    @PostConstruct
    public void init() {
        initService.init();
    }


    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final MemberService memberService;
        private final NoteService noteService;
        private final NoteRepository noteRepository;
        private final EntityManager em;


        public void init() {

            Note secondPrevNote = null;
            Note prevNote = null;

            /**
             * 100명의 회원과 100개의 노트 생성
             * 전 회원의 노트를 READ_WRITE
             * 전전 회원읜 노트를 READ_ONLY 할 수 있다.
             *
             * 예시)
             * member10
             * member9의 노트 -> read_write
             * member8의 노트 -> read_only
             */
            for (int i = 1; i <= 100; i++) {
                // member 100명 추가
                Member member = new Member("member" + i, "login" + i, "1234");
                memberService.join(member);

                Note note = new Note("note" + i, "content" + i);
                noteService.saveNote(note);

                MemberNote memberNote = new MemberNote(member, note, Permission.HOST);
                noteRepository.saveMemberNote(memberNote);

                if (i > 1) {
                    MemberNote memberNoteREAD_WRITE = new MemberNote(member, prevNote, Permission.READ_WRITE);
                    noteRepository.saveMemberNote(memberNoteREAD_WRITE);
                }

                if (i > 2) {
                    MemberNote memberNoteREAD_ONLY = new MemberNote(member, secondPrevNote, Permission.READ_ONLY);
                    noteRepository.saveMemberNote(memberNoteREAD_ONLY);
                }

                secondPrevNote = prevNote;
                prevNote = note;
            }
        }
    }
}
