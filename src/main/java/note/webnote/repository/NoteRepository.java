package note.webnote.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import note.webnote.domain.Member;
import note.webnote.domain.MemberNote;
import note.webnote.domain.Note;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class NoteRepository {

    private final EntityManager em;

    public void save(Note note) {
        em.persist(note);
    }

    public void saveMemberNote(MemberNote memberNote) {
        em.persist(memberNote);
    }

    /**
     * 노트 조회
     */
    public Optional<Note> findById(Long id) {
        return Optional.ofNullable(em.find(Note.class, id));
    }

    /**
     * 파라미터로 전달된 멤버의 모든 노트 조회
     */
    public List<MemberNote> findAllNoteByMember(Member member) {
        log.info("멤버가 가진 노트 전부 조회");
        return em.createQuery(
                        "select mn from MemberNote mn" +
                                " join fetch mn.note n" +
                                " join fetch mn.member m" +
                                " where mn.member = :member", MemberNote.class)
                .setParameter("member", member)
                .getResultList();
    }

}
