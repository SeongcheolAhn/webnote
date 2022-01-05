package note.webnote.web.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class MemberSaveForm {

    @NotEmpty
    private String name;

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String password;
}
