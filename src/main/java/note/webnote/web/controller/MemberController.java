package note.webnote.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import note.webnote.domain.Member;
import note.webnote.web.form.MemberSaveForm;
import note.webnote.web.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public String members() {
        log.info("member controller");
        return "home";
    }

    @GetMapping("/new")
    public String createMember(@ModelAttribute MemberSaveForm memberSaveForm) {
        log.info("create member");
        return "members/addMemberForm";
    }

    @PostMapping("/new")
    public String addMember(@ModelAttribute MemberSaveForm memberSaveForm, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "members/addMemberForm";
        }

        Member member = new Member(memberSaveForm.getName(), memberSaveForm.getLoginId(), memberSaveForm.getPassword());
        Long memberId = memberService.join(member);

        System.out.println("member = " + member);
        return "redirect:/";
    }

}
