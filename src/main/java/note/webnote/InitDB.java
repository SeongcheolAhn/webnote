package note.webnote;

import lombok.RequiredArgsConstructor;
import note.webnote.domain.Member;
import note.webnote.domain.MemberNote;
import note.webnote.domain.Note;
import note.webnote.domain.Permission;
import note.webnote.repository.MemberRepository;
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

    @PostConstruct
    public void init() {
        initService.init();
    }


    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final MemberService memberService;
        private final NoteService noteService;
        private final EntityManager em;

        public void init() {
            Member member = new Member("memberA", "loginA", "1234");
            memberService.join(member);

            Note note = new Note("NoteA", "contentA");
            noteService.saveNote(note);

            MemberNote memberNote = new MemberNote(member, note, Permission.HOST);
            em.persist(memberNote);
        }
    }
}
