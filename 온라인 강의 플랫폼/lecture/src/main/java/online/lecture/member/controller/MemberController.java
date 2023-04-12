package online.lecture.member.controller;

import lombok.RequiredArgsConstructor;
import online.lecture.member.controller.domain.JoinMemberForm;
import online.lecture.member.controller.domain.LoginMemberForm;
import online.lecture.member.controller.domain.UpdateMemberForm;
import online.lecture.member.entity.Member;
import online.lecture.member.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/member/")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("join")
    public String join(Model model){
        model.addAttribute("form",new JoinMemberForm());
        return "member/join";
    }

    @PostMapping("join")
    public String joinsuccess(@Validated @ModelAttribute("form") JoinMemberForm form, BindingResult br){
        if(!form.getPassword1().equals(form.getPassword2())){
            br.rejectValue("password2",null);
        }

        if(br.hasErrors()){
            return "member/join";
        }

        Member member = new Member(form.getUserId(),form.getUsername(),form.getPassword1(),form.getEmail());
        memberService.join(member);
        return "member/join-success";
    }

    @GetMapping("login")
    public String login(Model model,HttpSession session){
        if(session.getAttribute("memberId")!=null)
            return "member/login-already";
        model.addAttribute("form",new LoginMemberForm());
        return "member/login";
    }

    @PostMapping("login")
    public String loginSuccess(@Validated @ModelAttribute("form") LoginMemberForm form, HttpSession session){
        Member loginMember = memberService.login(form.getUserId(), form.getPassword());
        if(loginMember!=null){
            session.setAttribute("memberId",loginMember.getId());
            return "member/login-success";
        }else{
            return "member/login-fail";
        }
    }
}
