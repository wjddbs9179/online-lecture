package online.lecture.lecture.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.lecture.entity.Member;
import online.lecture.entity.MemberLecture;
import online.lecture.entity.category.SubCategory;
import online.lecture.lecture.controller.domain.RegLectureForm;
import online.lecture.lecture.controller.file.FileStore;
import online.lecture.lecture.controller.file.UploadFile;
import online.lecture.entity.Lecture;
import online.lecture.entity.Video;
import online.lecture.lecture.service.LectureService;
import online.lecture.member.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/lecture/")
@RequiredArgsConstructor
@Slf4j
public class LectureController {

    private final LectureService lectureService;
    private final MemberService memberService;
    private final FileStore fileStore;
    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("reg-lecture")
    public String regLecture(Model model){
        model.addAttribute("form",new RegLectureForm());
        return "lecture/reg-form";
    }

    @PostMapping("reg-lecture")
    public String regLecture(@ModelAttribute("form") RegLectureForm form, @RequestParam("subCategory")String subCategory,HttpServletRequest request) throws IOException {

        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());
        SubCategory realSubCategory = form.getCategory().getSubCategories().stream().filter(sc -> sc.getKorName().equals(subCategory)).findAny().orElse(null);

        Lecture lecture = new Lecture(form.getName(),form.getCategory(),realSubCategory,attachFile.getStoreFilename(),form.getIntro());

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

    @GetMapping("video/{id}")
    public String videoPlay(Model model, @PathVariable("id") Long id){
        log.info("id = {} ",id);
        Video video = lectureService.findVideo(id);

        model.addAttribute("video",video);
        return "lecture/video-play";
    }

    @GetMapping("filter/{category}")
    public String homeFilter(Model model, @PathVariable("category")String category){
        List<Lecture> recentLecture = lectureService.filter(category);

        model.addAttribute("fileDir",fileDir);
        model.addAttribute("recentLecture",recentLecture);
        return "home";
    }

    @ResponseBody
    @GetMapping("images/{filename}")
    public Resource imageView(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:"+fileStore.getFullPath(filename));
    }

    @GetMapping("info/{id}")
    public String info(Model model, @PathVariable Long id,HttpSession session){
        Lecture lecture = lectureService.info(id);
        model.addAttribute(lecture);
        Long memberId = (Long)session.getAttribute("memberId");

        Member member = memberService.info(memberId);
        MemberLecture ml = member.getLectures().stream()
                .filter(memberLecture -> memberLecture.getMember().getId() == memberId
                        && memberLecture.getLecture().getId() == id)
                .findAny().orElse(null);

        if(ml!=null){
            model.addAttribute("enrolment",true);
        }else {
            model.addAttribute("enrolment",false);
        }

        return "lecture/info";
    }

    @GetMapping("next-video/{lectureId}/{videoId}")
    public String nextVideo(Model model, @PathVariable("lectureId")Long lectureId,@PathVariable("videoId")Long videoId){

        Video video = lectureService.nextVideo(lectureId,videoId);

        if(video==null)
            return "lecture/lastVideo";

        model.addAttribute("video",video);
        return "lecture/video-play";
    }

    @GetMapping("prev-video/{lectureId}/{videoId}")
    public String prevVideo(Model model, @PathVariable("lectureId")Long lectureId,@PathVariable("videoId")Long videoId){
        Video video = lectureService.prevVideo(lectureId, videoId);

        if(video==null)
            return "lecture/firstVideo";

        model.addAttribute("video",video);
        return "lecture/video-play";
    }
}
