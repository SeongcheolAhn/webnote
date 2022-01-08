package note.webnote.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import note.webnote.domain.Member;
import note.webnote.web.form.MemberSaveForm;
import note.webnote.web.intercptor.SessionMember;
import note.webnote.web.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public String members() {
        log.info("[GET]    member controller");
        return "home";
    }

    @GetMapping("/new")
    public String createMemberForm(@ModelAttribute MemberSaveForm memberSaveForm) {
        log.info("[GET]    createMemberForm");
        return "members/addMemberForm";
    }

    @PostMapping("/new")
    public String createMember(@ModelAttribute MemberSaveForm memberSaveForm, BindingResult bindingResult) {

        log.info("[POST]    createMember");

        if(bindingResult.hasErrors()) {
            return "members/addMemberForm";
        }

        Member member = new Member(memberSaveForm.getName(), memberSaveForm.getLoginId(), memberSaveForm.getPassword());
        memberService.join(member);

        System.out.println("member = " + member);
        return "redirect:/";
    }

    @GetMapping("/{memberId}")
    public String memberHome(@PathVariable Long memberId, Model model, HttpServletRequest request) {

        // 본인이 맞는지 확인
        SessionMember loginMember = (SessionMember) request.getSession().getAttribute("LoginMember");
        if (!loginMember.getId().equals(memberId)) {
            log.info("본인 외 접근");
            log.info("memberId = {}",memberId);
            log.info("loginMemberId = {}", loginMember.getId());
            return "home";
        }

        model.addAttribute("notes", memberService.findNotes(memberId));
        return "members/viewMemberNote";
    }

}
