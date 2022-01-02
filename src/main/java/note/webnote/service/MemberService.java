package note.webnote.service;

import lombok.RequiredArgsConstructor;
import note.webnote.domain.Member;
import note.webnote.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 가입
    public Long join(Member member) {

        // 중복 검증
        List<Member> findMember = memberRepository.findByLoginId(member.getLoginId());
        if (!findMember.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

        memberRepository.save(member);
        return member.getId();
    }

    // 회원 조회
    public Member findOne(Long id) {
        return memberRepository.findById(id);
    }

    // 모든 회원 조회
    public List<Member> findMembers() {
        return memberRepository.findByAll();
    }
}
