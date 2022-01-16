package note.webnote.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import note.webnote.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MemberRepositoryOld {

    private final EntityManager em;

    // 완료
    public void save(Member member) {
        em.persist(member);
    }

    // 완료
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    // 완료
    public Optional<Member> findByLoginId(String memberLoginId) {
        log.info("findByLoginId");
        return findByAll().stream()
                .filter(m -> m.getLoginId().equals(memberLoginId))
                .findFirst();
    }

    // 완료
    public List<Member> findByAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    // 완료
    public Optional<Member> findByMemberName(String memberName) {
        return findByAll().stream()
                .filter(m -> m.getName().equals(memberName))
                .findFirst();
    }
}
