package note.webnote.repository;

import lombok.RequiredArgsConstructor;
import note.webnote.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    public List<Member> findByLoginId(String memberLoginId) {
        return em.createQuery("select m from Member m where m.loginId = :loginId", Member.class)
                .setParameter("loginId", memberLoginId)
                .getResultList();
    }

    public List<Member> findByAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
}
