package note.webnote.web.service;

import lombok.RequiredArgsConstructor;
import note.webnote.domain.Member;
import note.webnote.repository.MemberRepository;
import note.webnote.web.form.LoginForm;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private MemberRepository memberRepository;

    public Member login(String loginId, String password) {
        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);

    }
}
