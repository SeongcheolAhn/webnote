package note.webnote.web.intercptor;

import lombok.Getter;

/**
 * 세션에 사용할 멤버 클래스
 */
@Getter
public class SessionMember {
    private Long id;

    public SessionMember(Long id) {
        this.id = id;
    }
}
