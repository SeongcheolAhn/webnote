package note.webnote;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public class BCryptTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void passwordEncode() {
        // given
        String password = "1234";

        // when
        String encodedPassword = passwordEncoder.encode(password);

        // then
        boolean matches = passwordEncoder.matches(password, encodedPassword);
        Assertions.assertThat(matches).isTrue();
    }
}
