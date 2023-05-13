package online.lecture.admin.controller;

import lombok.RequiredArgsConstructor;
import online.lecture.admin.controller.domain.AdminCommentUpdateForm;
import online.lecture.admin.controller.domain.AdminCommentWriteForm;
import online.lecture.admin.controller.domain.AdminLoginForm;
import online.lecture.admin.service.AdminService;
import online.lecture.entity.AdminComment;
import online.lecture.entity.Lecture;
import online.lecture.entity.TeacherComment;
import online.lecture.entity.Video;
import online.lecture.entity.category.SubCategory;
import online.lecture.entity.member.Admin;
import online.lecture.entity.member.Teacher;
import online.lecture.lecture.controller.domain.RegLectureForm;
import online.lecture.lecture.controller.domain.TeacherCommentUpdateForm;
import online.lecture.lecture.controller.domain.TeacherCommentWriteForm;
import online.lecture.lecture.controller.file.FileStore;
import online.lecture.lecture.controller.file.UploadFile;
import online.lecture.lecture.service.LectureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/")
public class AdminController {

    private final AdminService adminService;
    private final LectureService lectureService;
    private final FileStore fileStore;
    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("home")
    public String home(Model model){

        List<Lecture> waitingLecture = adminService.getWaitingLecture();
        model.addAttribute("waitingLecture",waitingLecture);
        return "admin/home";
    }


    @GetMapping("login")
    public String login(Model model, @ModelAttribute("form") AdminLoginForm form) {
        adminService.postAdminJoin();
        return "admin/login";
    }

    @PostMapping("login")
    public String login(@ModelAttribute("form") AdminLoginForm form, HttpSession session) {
        Admin admin = adminService.login(form);

        session.setAttribute("memberId",null);
        session.setAttribute("teacherId",null);
        session.setAttribute("adminId", admin.getId());

        return "redirect:/";
    }

    @GetMapping("pubTrue/{id}")
    public String pubTrue(@PathVariable("id")Long id){
        adminService.pubTrue(id);
        return "redirect:/lecture/info/"+id;
    }

    @GetMapping("pubFalse/{id}")
    public String pubFalse(@PathVariable("id")Long id){
        adminService.pubFalse(id);
        return "redirect:/lecture/info/"+id;
    }

    @GetMapping("deleteLecture/{id}")
    public String deleteLecture(@PathVariable("id")Long id){
        adminService.deleteLecture(id);
        return "redirect:/admin/home";
    }

    @GetMapping("lecture/reg")
    public String regLecture(Model model){
        model.addAttribute("form",new RegLectureForm());
        return "lecture/reg-form";
    }

    @PostMapping("lecture/reg")
    public String regLecture(@Validated @ModelAttribute("form") RegLectureForm form, BindingResult br, @RequestParam(value = "subCategory",defaultValue = "")String subCategory, HttpServletRequest request, HttpSession session) throws IOException {
        SubCategory realSubCategory = form.getCategory().getSubCategories().stream().filter(sc -> sc.getKorName().equals(subCategory)).findAny().orElse(null);

        if(realSubCategory==null)
            return "lecture/empty-subCategory";

        if(br.hasErrors()){
            return "lecture/reg-form";
        }

        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());

        if(attachFile==null)
            return "lecture/attachFile-null";

        Lecture lecture = new Lecture(form.getName(),form.getCategory(),realSubCategory,attachFile.getStoreFilename(),form.getIntro(),null);

        lecture.setPub(false);

        String videoRoutes[] = request.getParameterValues("videoRoute");
        String videoNames[] = request.getParameterValues("videoName");
        int videoNameCount = 0;
        List<Video> videos = new ArrayList<>();
        for(String videoRoute : videoRoutes){
            Video video = new Video();
            if(videoNames[videoNameCount].equals(""))
                return "lecture/empty-videoName";
            video.setName(videoNames[videoNameCount++]);
            if(videoRoute.equals(""))
                return "lecture/empty-videoRoute";
            video.setVideoRoute(videoRoute);
            lecture.addVideo(video);
            videos.add(video);
        }

        lectureService.regLecture(lecture,videos);

        return "redirect:/";
    }

    @GetMapping("review/adminComment/write/{reviewId}")
    public String teacherCommentWrite(@PathVariable("reviewId")Long reviewId, HttpSession session,Model model){

        if(session.getAttribute("adminId")!=null){
            model.addAttribute("form",new AdminCommentWriteForm());
            return "admin/adminComment-write";
        }else{
            return "admin/adminComment-validAdmin-false";
        }
    }

    @PostMapping("review/adminComment/write/{reviewId}")
    public String teacherCommentWrite(@Validated @ModelAttribute("form") AdminCommentWriteForm form, BindingResult br,
                                      @PathVariable("reviewId")Long reviewId,HttpSession session){

        if(br.hasErrors())
            return "admin/adminComment-write";

        Long lectureId = adminService.adminCommentWrite(form.getContent(),reviewId,(Long)session.getAttribute("adminId"));
        return "redirect:/lecture/review/read/"+lectureId;
    }

    @GetMapping("adminComment/update/{adminCommentId}")
    public String teacherCommentUpdate(@PathVariable("adminCommentId")Long adminCommentId, HttpSession session,Model model){
        Long adminId = (Long)session.getAttribute("adminId");

        if(adminId!=null){
            AdminComment adminComment = adminService.getAdminComment(adminCommentId);

            model.addAttribute("form",new TeacherCommentUpdateForm(adminComment.getContent()));
            return "admin/adminComment-update";
        }else{
            return "admin/adminComment-validAdmin-false";
        }
    }

    @PostMapping("adminComment/update/{adminCommentId}")
    public String teacherCommentUpdate(@Validated @ModelAttribute("form") AdminCommentUpdateForm form, BindingResult br,
                                       @PathVariable("adminCommentId")Long adminCommentId, HttpSession session){
        Long adminId = (Long)session.getAttribute("adminId");

        if(adminId!=null){
            if(br.hasErrors())
                return "admin/adminComment-update";

            Long lectureId = adminService.adminCommentUpdate(adminCommentId,form.getContent());
            return "redirect:/lecture/review/read/"+lectureId;
        }else{
            return "admin/adminComment-validAdmin-false";
        }
    }
    @GetMapping("adminComment/delete/{adminCommentId}")
    public String teacherCommentDelete(@PathVariable("adminCommentId")Long adminCommentId,HttpSession session){
        Long adminId = (Long)session.getAttribute("adminId");

        if(adminId!=null){
            Long lectureId = adminService.deleteAdminComment(adminCommentId);
            return "redirect:/lecture/review/read/"+lectureId;
        }else{
            return "admin/adminComment-validAdmin-false";
        }
    }

//    @GetMapping("sampleDataCreate")
    public String sampleDataCreate(){

        lectureService.sampleDataCreate();
        return "redirect:/";
    }
}
