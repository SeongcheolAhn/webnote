package note.webnote;

import note.webnote.domain.Member;
import note.webnote.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@Transactional
public class MariaDBTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void connect() {
        // given
        Member member = new Member("memberA", "loginA", "1234");
        memberRepository.save(member);

        // when
        Optional<Member> result = memberRepository.findById(member.getId());

        // then
        Member findMember = result.orElse(null);
        Assertions.assertThat(findMember).isEqualTo(member);
    }
}
