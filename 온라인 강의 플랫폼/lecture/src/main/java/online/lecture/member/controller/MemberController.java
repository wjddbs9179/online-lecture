package online.lecture.member.controller;

import lombok.RequiredArgsConstructor;
import online.lecture.entity.Lecture;
import online.lecture.entity.member.Teacher;
import online.lecture.lecture.controller.domain.RegLectureForm;
import online.lecture.member.controller.domain.*;
import online.lecture.entity.member.Member;
import online.lecture.member.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/member/")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("join")
    public String join(Model model) {
        model.addAttribute("form", new JoinMemberForm());
        return "member/join";
    }

    @PostMapping("join")
    public String joinsuccess(@Validated @ModelAttribute("form") JoinMemberForm form, BindingResult br, @RequestParam("memberDivision") String memberDivision) {

        if (!memberService.userIdUniqueCheck(form.getUserId(),memberDivision))
            br.rejectValue("userId", null, "중복된 회원아이디 입니다.");


        if (!form.getPassword1().equals(form.getPassword2())) {
            br.rejectValue("password2", null, "비밀번호와 일치하지 않습니다.");
        }

        if (br.hasErrors()) {
            return "member/join";
        }

        if (memberDivision.equals("basic")) {
            Member member = new Member(form.getUserId(), form.getUsername(), form.getPassword1(), form.getEmail());
            memberService.join(member);
        }
        if(memberDivision.equals("teacher")){
            Teacher teacher = new Teacher(form.getUserId(),form.getUsername(),form.getPassword1(),form.getEmail());
            memberService.joinTeacher(teacher);
        }

        return "member/join-success";
    }

    @GetMapping("login")
    public String login(Model model, HttpSession session) {
        if (session.getAttribute("memberId") != null || session.getAttribute("teacherId")!=null)
            return "member/login-already";
        model.addAttribute("form", new LoginMemberForm());
        return "member/login";
    }

    @PostMapping("login")
    public String loginSuccess(@Validated @ModelAttribute("form") LoginMemberForm form, HttpSession session,@RequestParam("division")String division) {
        if(division.equals("basic")) {
            Member loginMember = memberService.login(form.getUserId(), form.getPassword());
            if (loginMember != null) {
                session.setAttribute("memberId", loginMember.getId());
                return "member/login-success";
            }
        }

        if(division.equals("teacher")){
            Teacher loginTeacher = memberService.loginTeacher(form.getUserId(), form.getPassword());
            if (loginTeacher != null) {
                session.setAttribute("teacherId", loginTeacher.getId());
                return "member/login-success";
            }
        }
        return "member/login-fail";
    }

    @GetMapping("id-search")
    public String idSearch(Model model) {
        model.addAttribute("form", new IdSearchForm());
        return "member/id-search-form";
    }

    @PostMapping("id-search")
    public String idSearch(Model model, @Validated @ModelAttribute("form") IdSearchForm form, BindingResult br) {
        if (br.hasErrors())
            return "member/id-search-form";

        Member findMember = memberService.idSearch(form);
        if (findMember != null) {
            model.addAttribute("userId", findMember.getUserId());
            model.addAttribute("cNum", (int) (Math.random() * 899999) + 100000);
            return "member/id-search-cNum";
        } else {
            return "member/id-search-fail";
        }
    }

    @PostMapping("cNumCheck-Id")
    public String idSearch(Model model, @RequestParam String userId, @RequestParam int cNum, @RequestParam int cNumCheck) {

        if (cNum != cNumCheck)
            return "member/cNumCheck-fail";


        model.addAttribute("userId", userId);
        return "member/cNum-check-com-id";
    }

    @GetMapping("pw-search")
    public String pwSearch(Model model) {
        model.addAttribute("form", new PwSearchForm());
        return "member/pw-search-form";
    }

    @PostMapping("pw-search")
    public String pwSearch(Model model, @Validated @ModelAttribute("form") PwSearchForm form, BindingResult br) {
        if (br.hasErrors())
            return "member/pw-search-form";

        Member findMember = memberService.pwSearch(form);
        if (findMember != null) {
            model.addAttribute("password", findMember.getPassword());
            model.addAttribute("cNum", (int) (Math.random() * 899999) + 100000);
            return "member/pw-search-cNum";
        } else {
            return "member/pw-search-fail";
        }

    }

    @PostMapping("cNumCheck-pw")
    public String cNumCheck_pw(Model model, @RequestParam String password, @RequestParam int cNum, @RequestParam int cNumCheck) {
        if (cNum != cNumCheck)
            return "member/cNumCheck-fail";

        model.addAttribute("password", password);
        return "member/cNum-check-com-pw";
    }

    @GetMapping("logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("myInfo")
    public String myInfo(Model model, HttpSession session) {
        Long id = (Long) session.getAttribute("memberId");
        Member member = memberService.info(id);
        model.addAttribute(member);
        return "member/myInfo";
    }

    @GetMapping("myInfo/teacher")
    public String myInfo_teacher(Model model, HttpSession session) {
        Long id = (Long) session.getAttribute("teacherId");
        Teacher teacher = memberService.info_teacher(id);
        model.addAttribute("member",teacher);
        return "member/myInfo";
    }

    @GetMapping("update/check")
    public String updateCheck() {
        return "member/update-check";
    }

    @PostMapping("update/check")
    public String updateCheck(Model model, @RequestParam String password, HttpSession session) {
        if(session.getAttribute("memberId")!=null) {
            Long memberId = (Long) session.getAttribute("memberId");

            Member member = memberService.info(memberId);
            if (member.getPassword().equals(password)) {
                UpdateMemberForm form = new UpdateMemberForm();
                form.setEmail(member.getEmail());
                form.setPassword(member.getPassword());
                form.setUsername(member.getUsername());
                model.addAttribute("form", form);
                return "member/update-form";
            }

        }
        if(session.getAttribute("teacherId")!=null){
            Long teacherId = (Long) session.getAttribute("teacherId");

            Teacher teacher = memberService.info_teacher(teacherId);
            if (teacher.getPassword().equals(password)) {
                UpdateMemberForm form = new UpdateMemberForm();
                form.setEmail(teacher.getEmail());
                form.setPassword(teacher.getPassword());
                form.setUsername(teacher.getUsername());
                model.addAttribute("form", form);
                return "member/update-form";
            }
        }
        return "member/update-check-fail";
    }

    @PostMapping("update")
    public String update(@Validated @ModelAttribute("form") UpdateMemberForm form,BindingResult br, HttpSession session,Model model) {
        if(session.getAttribute("memberId")!=null) {
            Long memberId = (Long) session.getAttribute("memberId");
            memberService.update(memberId, form);
            model.addAttribute("division","basic");
        }
        if(session.getAttribute("teacherId")!=null){
            Long teacherId = (Long) session.getAttribute("teacherId");
            memberService.updateTeacher(teacherId,form);
            model.addAttribute("division","teacher");
        }
        if(br.hasErrors())
            return "member/update-form";
        return "member/update-com";
    }

    @GetMapping("drop")
    public String dropMember() {
        return "member/drop-pw-check";
    }

    @PostMapping("drop")
    public String dropMember(@RequestParam(defaultValue = "") String password, HttpSession session) {
        if(session.getAttribute("memberId")!=null) {
            Long memberId = (Long) session.getAttribute("memberId");

            Member member = memberService.info(memberId);
            if (member.getPassword().equals(password)) {
                memberService.delete(memberId);
                session.invalidate();
                return "member/delete-com";
            }
        }
        if(session.getAttribute("teacherId")!=null){
            Long teacherId = (Long) session.getAttribute("teacherId");

            Teacher teacher = memberService.info_teacher(teacherId);
            if (teacher.getPassword().equals(password)) {
                memberService.deleteTeacher(teacherId);
                session.invalidate();
                return "member/delete-com";
            }
        }
        return "member/delete-fail";
    }

    @GetMapping("Enrolment/{id}")
    public String enrolment(@PathVariable("id") Long lectureId, HttpSession session) {
        if(session.getAttribute("memberId")!=null) {
            Long memberId = (Long) session.getAttribute("memberId");


            memberService.enrolment(memberId, lectureId);
        }
        if(session.getAttribute("teacherId")!=null){
            return "member/enrolment-teacher";
        }
        return "redirect:/lecture/info/" + lectureId;
    }

    @GetMapping("myLecture")
    public String myLecture(Model model, HttpSession session) {
        Long memberId = (Long) session.getAttribute("memberId");
        List<Lecture> lectures = memberService.myLecture(memberId);
        model.addAttribute("myLecture", lectures);
        return "member/my-lecture";
    }

    @GetMapping("myLecture/teacher")
    public String myLectureTeacher(Model model, HttpSession session){
        Long teacherId = (Long) session.getAttribute("teacherId");
        List<Lecture> lectures = memberService.myLectureTeacher(teacherId);
        model.addAttribute("myLecture",lectures);
        return "member/my-lecture";
    }
}
