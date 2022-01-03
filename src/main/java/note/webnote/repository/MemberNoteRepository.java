package note.webnote.repository;

import lombok.RequiredArgsConstructor;
import note.webnote.domain.Member;
import note.webnote.domain.MemberNote;
import note.webnote.domain.Note;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberNoteRepository {

    private final EntityManager em;

    public void save(MemberNote memberNote) {
        em.persist(memberNote);
    }

    public Optional<MemberNote> findById(Long id) {
        return Optional.ofNullable(em.find(MemberNote.class, id));
    }

    public List<MemberNote> findByNote(Note note) {
        return em.createQuery("select mn from MemberNote mn where mn.note = :note", MemberNote.class)
                .setParameter("note", note)
                .getResultList();
    }

    public List<MemberNote> findByMember(Member member) {
        return em.createQuery("select mn from MemberNote mn where mn.member = :member", MemberNote.class)
                .setParameter("member", member)
                .getResultList();
    }
}
