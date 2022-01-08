package note.webnote.web.controller;

import note.webnote.web.intercptor.SessionMember;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpServletRequest request) {

        // 비로그인 사용자
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("LoginMember") == null) {
            return "home";
        }

        SessionMember loginMember = (SessionMember) session.getAttribute("LoginMember");
        Long memberId = loginMember.getId();

        return "redirect:/members/" + memberId;
    }
}
