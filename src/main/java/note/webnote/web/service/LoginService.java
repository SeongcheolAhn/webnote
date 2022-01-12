package note.webnote.web.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import note.webnote.domain.Member;
import note.webnote.repository.MemberRepository;
import note.webnote.web.form.LoginForm;
import note.webnote.web.intercptor.SessionMember;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final MemberRepository memberRepository;

    public Member login(String loginId, String password) {
        log.info("login service");
        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);

    }

    /**
     * 현재 로그인 한 회원의 ID 반환
     */
    public Long checkMember(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        SessionMember loginMember = (SessionMember) session.getAttribute("LoginMember");
        return loginMember.getId();
    }
}
