package online.lecture.admin.controller;

import lombok.RequiredArgsConstructor;
import online.lecture.admin.controller.domain.AdminLoginForm;
import online.lecture.admin.service.AdminService;
import online.lecture.entity.Lecture;
import online.lecture.entity.Video;
import online.lecture.entity.category.SubCategory;
import online.lecture.entity.member.Admin;
import online.lecture.entity.member.Teacher;
import online.lecture.lecture.controller.domain.RegLectureForm;
import online.lecture.lecture.controller.file.FileStore;
import online.lecture.lecture.controller.file.UploadFile;
import online.lecture.lecture.service.LectureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

        session.setAttribute("adminId", admin.getId());

        return "redirect:/admin/home";
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
    public String regLecture(@ModelAttribute("form") RegLectureForm form, @RequestParam("subCategory")String subCategory, HttpServletRequest request, HttpSession session) throws IOException {

        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());
        SubCategory realSubCategory = form.getCategory().getSubCategories().stream().filter(sc -> sc.getKorName().equals(subCategory)).findAny().orElse(null);

        Lecture lecture = new Lecture(form.getName(),form.getCategory(),realSubCategory,attachFile.getStoreFilename(),form.getIntro(),null);

        lecture.setPub(false);

        String videoRoutes[] = request.getParameterValues("videoRoute");
        String videoNames[] = request.getParameterValues("videoName");
        int videoNameCount = 0;
        List<Video> videos = new ArrayList<>();
        for(String videoRoute : videoRoutes){
            Video video = new Video();
            video.setName(videoNames[videoNameCount++]);
            video.setVideoRoute(videoRoute);
            lecture.addVideo(video);
            videos.add(video);
        }
        lectureService.regLecture(lecture,videos);

        return "redirect:/";
    }
}
