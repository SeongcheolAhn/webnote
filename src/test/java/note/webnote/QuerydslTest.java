package note.webnote;

import com.querydsl.jpa.impl.JPAQueryFactory;
import note.webnote.domain.*;
import note.webnote.repository.MemberRepository;
import note.webnote.repository.NoteRepository;
import note.webnote.web.dto.MemberHomeCondition;
import note.webnote.web.dto.MemberHomeMemberNoteDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@Transactional
public class QuerydslTest {

    @Autowired
    EntityManager em;

    @Autowired
    NoteRepository noteRepository;
    @Autowired
    MemberRepository memberRepository;


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

    /**
     * 멤버의 전체 노트 조회시 동적 쿼리 동작 테스트
     */
    @Test
    void memberHomeTest() {

        // given
        Member memberA = new Member("memberA", "loginA", "1234");
        memberRepository.save(memberA);
        Member memberB = new Member("memberB", "loginB", "1234");
        memberRepository.save(memberB);
        Member memberC = new Member("memberC", "loginC", "1234");
        memberRepository.save(memberC);

        Note note = new Note("titleA", "contentA");
        noteRepository.save(note);

        MemberNote memberNoteA = new MemberNote(memberA, note, Permission.HOST);
        noteRepository.saveMemberNote(memberNoteA);
        MemberNote memberNoteB = new MemberNote(memberB, note, Permission.READ_ONLY);
        noteRepository.saveMemberNote(memberNoteB);
        MemberNote memberNoteC = new MemberNote(memberC, note, Permission.READ_ONLY);
        noteRepository.saveMemberNote(memberNoteC);

        // Pageable
        PageRequest pageRequest = PageRequest.of(0, 5);

        // when
        MemberHomeCondition condition = new MemberHomeCondition();
        condition.setPermission("HOST");

        Page<MemberHomeMemberNoteDto> result = noteRepository.findMemberNoteDto(memberA.getId(), condition, pageRequest);

        // then
        Assertions.assertThat(result.getContent().size()).isEqualTo(1);

    }
}
