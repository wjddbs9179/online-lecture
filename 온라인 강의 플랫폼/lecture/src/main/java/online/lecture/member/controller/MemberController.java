package online.lecture.member.controller;

import lombok.RequiredArgsConstructor;
import online.lecture.member.controller.domain.*;
import online.lecture.member.entity.Member;
import online.lecture.member.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
            br.rejectValue("password2",null,"비밀번호와 일치하지 않습니다.");
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

    @GetMapping("id-search")
    public String idSearch(Model model){
        model.addAttribute("form",new IdSearchForm());
        return "member/id-search-form";
    }

    @PostMapping("id-search")
    public String idSearch(Model model, @Validated @ModelAttribute("form") IdSearchForm form,BindingResult br){
        if(br.hasErrors())
            return "member/id-search-form";

        Member findMember = memberService.idSearch(form);
        if(findMember!=null){
            model.addAttribute("userId",findMember.getUserId());
            model.addAttribute("cNum",(int)(Math.random()*899999)+100000);
            return "member/id-search-cNum";
        }else{
            return "member/id-search-fail";
        }
    }

    @PostMapping("cNumCheck-Id")
    public String idSearch(Model model, @RequestParam String userId,@RequestParam int cNum,@RequestParam int cNumCheck){

        if(cNum != cNumCheck)
            return "member/cNumCheck-fail";


        model.addAttribute("userId",userId);
        return "member/cNum-check-com-id";
    }

    @GetMapping("pw-search")
    public String pwSearch(Model model){
        model.addAttribute("form",new PwSearchForm());
        return "member/pw-search-form";
    }

    @PostMapping("pw-search")
    public String pwSearch(Model model, @Validated @ModelAttribute("form") PwSearchForm form, BindingResult br){
        if(br.hasErrors())
            return "member/pw-search-form";

        Member findMember = memberService.pwSearch(form);
        if(findMember!=null){
            model.addAttribute("password",findMember.getPassword());
            model.addAttribute("cNum",(int)(Math.random()*899999)+100000);
            return "member/pw-search-cNum";
        }else{
            return "member/pw-search-fail";
        }

    }

    @PostMapping("cNumCheck-pw")
    public String cNumCheck_pw(Model model, @RequestParam String password,@RequestParam int cNum,@RequestParam int cNumCheck){
        if(cNum!=cNumCheck)
            return "member/cNumCheck-fail";

        model.addAttribute("password",password);
        return "member/cNum-check-com-pw";
    }

    @GetMapping("logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("myInfo")
    public String myInfo(Model model,HttpSession session){
        Long id = (Long) session.getAttribute("memberId");
        Member member = memberService.info(id);
        model.addAttribute(member);
        return "member/myInfo";
    }

    @GetMapping("update/check")
    public String updateCheck(){
        return "member/update-check";
    }

    @PostMapping("update/check")
    public String updateCheck(Model model,@RequestParam String password, HttpSession session){
        Long memberId = (Long)session.getAttribute("memberId");

        Member member = memberService.info(memberId);
        if(member.getPassword().equals(password)){
            UpdateMemberForm form = new UpdateMemberForm();
            form.setEmail(member.getEmail());
            form.setPassword(member.getPassword());
            form.setUsername(member.getUsername());
            model.addAttribute("form",form);
            return "member/update-form";
        }
        return "member/update-check-fail";
    }

    @PostMapping("update")
    public String update(@ModelAttribute("form") UpdateMemberForm form,HttpSession session){
        Long memberId = (Long)session.getAttribute("memberId");
        memberService.update(memberId,form);
        return "member/update-com";
    }

    @GetMapping("drop")
    public String dropMember(){
        return "member/drop-pw-check";
    }

    @PostMapping("drop")
    public String dropMember(@RequestParam String password,HttpSession session){
        Long memberId = (Long)session.getAttribute("memberId");

        Member member = memberService.info(memberId);
        if(member.getPassword().equals(password)){
            memberService.delete(memberId);
            session.invalidate();
            return "member/delete-com";
        }
        else{
            return "member/delete-fail";
        }
    }
}
