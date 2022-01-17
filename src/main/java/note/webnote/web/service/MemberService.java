package note.webnote.web.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import note.webnote.domain.Member;
import note.webnote.repository.MemberRepository;
import note.webnote.repository.NoteRepositoryOld;
import note.webnote.web.dto.MemberHomeDto;
import note.webnote.web.form.MemberSaveForm;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final NoteRepositoryOld noteRepositoryOld;
    private final PasswordEncoder passwordEncoder;

    // 회원 가입
    @Transactional
    public Long join(Member member) {

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(member.getPassword());
        member.setEncodedPassword(encodedPassword);

        // 중복 검증
        Optional<Member> findMember = memberRepository.findByLoginId(member.getLoginId());
        if (findMember.isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

        memberRepository.save(member);
        return member.getId();
    }

    // 회원 조회
    public Optional<Member> findOne(Long id) {
        return memberRepository.findById(id);
    }

    // 모든 회원 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 회원의 노트 전부 조회
    public MemberHomeDto findNotes(Long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if (findMember.isEmpty()) {
            log.info("Member 조회 불가능");
            return null;
        }
        return new MemberHomeDto(findMember.get());
    }
}
