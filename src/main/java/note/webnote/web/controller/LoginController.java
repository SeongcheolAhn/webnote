package note.webnote.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import note.webnote.domain.Member;
import note.webnote.web.form.LoginForm;
import note.webnote.web.intercptor.SessionMember;
import note.webnote.web.service.LoginService;
import note.webnote.web.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login/LoginForm";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult
            , HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return "login/LoginForm";
        }

        Member findMember = loginService.login(form.getLoginId(), form.getPassword());

        // 로그인 실패
        if (findMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/LoginForm";
        }

        // 로그인 성공
        request.getSession().setAttribute("LoginMember", new SessionMember(findMember.getId()));
        String redirectUrl = "/members/" + findMember.getId();
        log.info("로그인 성공");

        return "redirect:" + redirectUrl;

    }
}
