package note.webnote;

import com.querydsl.jpa.impl.JPAQueryFactory;
import note.webnote.domain.Member;
import note.webnote.domain.QMember;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
public class QuerydslTest {

    @Autowired
    EntityManager em;

    @Test
    void test() {
        Member member = new Member("test", "test", "1234");
        em.persist(member);

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QMember qMember = QMember.member;

        Member result = queryFactory
                .selectFrom(qMember)
                .where(
                        qMember.name.eq("test")
                )
                .fetchOne();

        Assertions.assertThat(result).isEqualTo(member);
    }
}
