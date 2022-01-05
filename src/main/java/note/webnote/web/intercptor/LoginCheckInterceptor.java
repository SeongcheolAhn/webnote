package note.webnote.web.intercptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("로그인 체크 인터셉터");

        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("LoginMember") == null) {
            log.info("비로그인 사용자");

            response.sendRedirect("/");
            return false;
        }

        return true;
    }
}
