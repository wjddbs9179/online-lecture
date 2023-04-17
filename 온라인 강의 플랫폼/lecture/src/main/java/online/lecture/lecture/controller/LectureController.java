package online.lecture.lecture.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.lecture.lecture.controller.domain.RegLectureForm;
import online.lecture.lecture.controller.file.FileStore;
import online.lecture.lecture.controller.file.UploadFile;
import online.lecture.lecture.entity.Lecture;
import online.lecture.lecture.entity.Video;
import online.lecture.lecture.service.LectureService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
    private final FileStore fileStore;

    @GetMapping("reg-lecture")
    public String regLecture(Model model){
        model.addAttribute("form",new RegLectureForm());
        return "lecture/reg-form";
    }

    @PostMapping("reg-lecture")
    public String regLecture(@ModelAttribute("form") RegLectureForm form, HttpServletRequest request) throws IOException {

        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());

        Lecture lecture = new Lecture(form.getName(),form.getCategory(),attachFile.getStoreFilename(),form.getIntro());

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
    public String videoPlay(Model model, @PathVariable Long id){
        log.info("id = {} ",id);
        Video video = lectureService.findVideo(id);

        log.info("videoRoute = {}", video.getVideoRoute());
        model.addAttribute("videoRoute",video.getVideoRoute());
        return "lecture/video-play";
    }

    @ResponseBody
    @GetMapping("images/{filename}")
    public Resource imageView(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:"+fileStore.getFullPath(filename));
    }

    @GetMapping("info/{id}")
    public String info(Model model, @PathVariable Long id){
        Lecture lecture = lectureService.info(id);
        model.addAttribute(lecture);

        return "lecture/info";
    }
}
