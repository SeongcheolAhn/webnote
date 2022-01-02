package note.webnote.service;

import note.webnote.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired EntityManager em;

    @Test
    public void 회원가입() {

        // given
        Member member = new Member("loginId", "1234", "memberA");

        // when
        Long saveMemberId = memberService.join(member);

        // then
        Member findMember = em.find(Member.class, saveMemberId);
        assertThat(member.getId()).isEqualTo(findMember.getId());
    }

    @Test
    public void 중복_회원_검증() {

        // given
        Member memberA = new Member("idA", "1234", "memberA");
        Member memberB = new Member("idA", "3222", "memberB");

        // when
        memberService.join(memberA);
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.join(memberB)); // 중복 예외 발생

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }
}