package note.webnote.service;

import note.webnote.domain.Member;
import note.webnote.web.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired EntityManager em;

    @Test
    void 회원가입() {

        // given
        Member member = new Member("memberA", "loginA", "1234");

        // when
        Long saveMemberId = memberService.join(member);

        // then
        Member findMember = em.find(Member.class, saveMemberId);
        assertThat(member.getId()).isEqualTo(findMember.getId());
    }

    @Test
    void 중복_회원_검증() {

        // given
        Member memberA = new Member("memberA", "loginA", "1234");
        Member memberB = new Member("memberA", "loginA", "2233");

        // when
        memberService.join(memberA);
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.join(memberB)); // 중복 예외 발생

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }

    @Test
    void 회원조회() {

        // given
        Member memberA = new Member("memberA", "loginA", "1234");
        Long memberAId = memberService.join(memberA);

        // when
        String findMemberName = memberService.findOne(memberAId)
                .map(Member::getName)
                .orElse("null");

        // then
        assertThat(findMemberName).isEqualTo(memberA.getName());
    }

    @Test
    void 모든_회원조회() {

        // given
        Member memberA = new Member("memberA", "loginA", "1234");
        Member memberB = new Member("memberB", "loginB", "2345");
        Member memberC = new Member("memberC", "loginC", "3456");

        memberService.join(memberA);
        memberService.join(memberB);
        memberService.join(memberC);

        // when
        List<Member> findMembers = memberService.findMembers();

        // then
//        assertThat(findMembers.size()).isEqualTo(3);
    }
}