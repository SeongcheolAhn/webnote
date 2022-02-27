package note.webnote;

import note.webnote.web.controller.HomeController;
import note.webnote.web.controller.MemberController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpServletRequest;

@SpringBootTest
public class LogTest {

    @Autowired
    HomeController homeController;

    @Autowired
    HttpServletRequest request;

    @Test
    void test() {
        homeController.home(request);
    }

}
