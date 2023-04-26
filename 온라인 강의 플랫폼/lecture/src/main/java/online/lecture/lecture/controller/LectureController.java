package online.lecture.lecture.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.lecture.admin.service.AdminService;
import online.lecture.entity.*;
import online.lecture.entity.member.Member;
import online.lecture.entity.category.SubCategory;
import online.lecture.entity.member.Teacher;
import online.lecture.lecture.controller.domain.*;
import online.lecture.lecture.controller.file.FileStore;
import online.lecture.lecture.controller.file.UploadFile;
import online.lecture.lecture.service.LectureService;
import online.lecture.member.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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

    private final AdminService adminService;
    private final LectureService lectureService;
    private final MemberService memberService;
    private final FileStore fileStore;
    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("reg-lecture")
    public String regLecture(Model model) {
        model.addAttribute("form", new RegLectureForm());
        return "lecture/reg-form";
    }

    @PostMapping("reg-lecture")
    public String regLecture(@Validated @ModelAttribute("form") RegLectureForm form, BindingResult br, @RequestParam("subCategory") String subCategory, HttpServletRequest request, HttpSession session) throws IOException {

        if (form.getAttachFile() == null) {
            br.rejectValue("attachFile", null, "메인 이미지를 선택해주세요.");
        }

        if (br.hasErrors())
            return "lecture/reg-form";


        Long teacherId = (Long) session.getAttribute("teacherId");

        Teacher teacher = memberService.info_teacher(teacherId);
        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());
        List<UploadFile> videoFiles = fileStore.storeFiles(form.getVideoFiles());


        SubCategory realSubCategory = null;

        if (form.getCategory().getSubCategories() != null) {
            realSubCategory = form.getCategory().getSubCategories().stream().filter(sc -> sc.getKorName().equals(subCategory)).findAny().orElse(null);
        }
        Lecture lecture = new Lecture(form.getName(), form.getCategory(), realSubCategory, attachFile.getStoreFilename(), form.getIntro(), teacher);

        lecture.setPub(false);

        String videoNames[] = request.getParameterValues("videoName");
        int videoNameCount = 0;
        List<Video> videos = new ArrayList<>();
        for (UploadFile videoFile : videoFiles) {
            Video video = new Video();
            video.setName(videoNames[videoNameCount++]);
            video.setVideoRoute(videoFile.getStoreFilename());
            lecture.addVideo(video);
            videos.add(video);
        }
        lectureService.regLecture(lecture, videos);

        return "redirect:/";
    }

    @GetMapping("video/{id}")
    public String videoPlay(Model model, @PathVariable("id") Long id, HttpSession session) {
        Video video = lectureService.findVideo(id);

        model.addAttribute("video", video);

        if (session.getAttribute("adminId") != null)
            return "lecture/video-play";

        if (video.getLecture().getTeacher() != null) {
            if (video.getLecture().getTeacher().getId().equals(session.getAttribute("teacherId"))) {
                return "lecture/video-play";
            }
        }

        if (!video.getLecture().isPub())
            return "lecture/video-notPub";
        else
            return "lecture/video-play";
    }

    @GetMapping("filter/{category}")
    public String homeFilter(Model model, @PathVariable("category") String category) {
        List<Lecture> recentLecture = lectureService.filter(category);

        model.addAttribute("fileDir", fileDir);
        model.addAttribute("recentLecture", recentLecture);
        return "home";
    }

    @ResponseBody
    @GetMapping("images/{filename}")
    public Resource imageView(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    @ResponseBody
    @GetMapping("videoFile/{filename}")
    public Resource videoFileView(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    @GetMapping("info/{id}")
    public String info(Model model, @PathVariable Long id, HttpSession session) {
        Lecture lecture = lectureService.info(id);

        if (session.getAttribute("adminId") == null) {
            if (!lecture.isPub() && lecture.getTeacher().equals(session.getAttribute("teacherId")))
                return "lecture/video-notPub";
        }
        model.addAttribute(lecture);

        Long teacherId = (Long) session.getAttribute("teacherId");
        Long memberId = (Long) session.getAttribute("memberId");
        Long adminId = (Long) session.getAttribute("adminId");

        if (adminId != null) {
            model.addAttribute("enrolment", true);
        } else if (memberId != null) {
            Member member = memberService.info(memberId);
            MemberLecture ml = member.getLectures().stream()
                    .filter(memberLecture -> memberLecture.getMember().getId() == memberId
                            && memberLecture.getLecture().getId() == id)
                    .findAny().orElse(null);

            if (ml != null) {
                model.addAttribute("enrolment", true);
            } else {
                model.addAttribute("enrolment", false);
            }
        } else if (teacherId != null) {
            Teacher teacher = memberService.info_teacher(teacherId);
            Lecture tl = teacher.getMyLectures().stream().filter(l -> l.getId().equals(lecture.getId()))
                    .findAny().orElse(null);

            if (tl != null) {
                model.addAttribute("enrolment", true);
            } else {
                model.addAttribute("enrolment", false);
            }
        }

        return "lecture/info";
    }

    @GetMapping("next-video/{lectureId}/{videoId}")
    public String nextVideo(Model model, @PathVariable("lectureId") Long lectureId, @PathVariable("videoId") Long videoId, HttpSession session) {

        Video video = lectureService.nextVideo(lectureId, videoId);
        model.addAttribute("video", video);

        if (video == null)
            return "lecture/lastVideo";

        if (session.getAttribute("adminId") != null) {
            return "lecture/video-play";
        }

        if (video.getLecture().getTeacher().getId().equals((Long) session.getAttribute("teacherId"))) {
            return "lecture/video-play";
        }

        if (!video.getLecture().isPub()) {
            return "lecture/video-notPub";
        }

        return "lecture/video-play";
    }

    @GetMapping("prev-video/{lectureId}/{videoId}")
    public String prevVideo(Model model, @PathVariable("lectureId") Long lectureId, @PathVariable("videoId") Long videoId, HttpSession session) {
        Video video = lectureService.prevVideo(lectureId, videoId);
        model.addAttribute("video", video);

        if (video == null)
            return "lecture/firstVideo";

        if (session.getAttribute("adminId") != null) {
            return "lecture/video-play";
        }

        if (video.getLecture().getTeacher().getId().equals((Long) session.getAttribute("teacherId"))) {
            return "lecture/video-play";
        }

        if (!video.getLecture().isPub()) {
            return "lecture/video-notPub";
        }

        return "lecture/video-play";
    }

    @GetMapping("review-write/{lectureId}")
    public String reviewWrite(@PathVariable("lectureId") Long lectureId, @ModelAttribute("form") ReviewWriteForm form, HttpSession session) {
        Member member = memberService.info((Long) session.getAttribute("memberId"));
        MemberLecture memberLecture = member.getLectures().stream()
                .filter(ml -> ml.getLecture().getId().equals(lectureId)).findAny().orElse(null);
        if (memberLecture != null) {
            return "lecture/review-write-form";
        }else{
            return "lecture/review-write-not-enrolment";
        }
    }

    @PostMapping("review-write/{lectureId}")
    public String reviewWrite(@PathVariable("lectureId") Long lectureId, HttpSession session,
                              @Validated @ModelAttribute("form") ReviewWriteForm form, BindingResult br) {

        if (br.hasErrors())
            return "lecture/review-write-form";

        Review review = new Review();
        Lecture lecture = lectureService.info(lectureId);
        Long memberId = (Long) session.getAttribute("memberId");
        Member member = memberService.info(memberId);

        MemberLecture memberLecture = member.getLectures().stream()
                .filter(ml -> ml.getLecture().getId().equals(lectureId)).findAny().orElse(null);
        if(memberLecture==null)
            return "lecture/review-write-not-enrolment";

        review.writeReview(lecture, member, form.getContent());

        lectureService.reviewWrite(review);

        return "redirect:/lecture/info/" + lectureId;
    }

    @GetMapping("review/read/{lectureId}")
    public String reviewRead(@PathVariable("lectureId") Long lectureId, Model model) {

        List<Review> reviews = lectureService.findReviews(lectureId);
        List<TeacherComment> teacherComments = lectureService.getTeacherComments(lectureId);
        List<AdminComment> adminComments = adminService.getAdminComments(lectureId);

        model.addAttribute("adminComments", adminComments);
        model.addAttribute("teacherComments", teacherComments);
        model.addAttribute("reviews", reviews);
        model.addAttribute("lectureId", lectureId);

        return "lecture/review-read";
    }

    @GetMapping("review/update/{reviewId}")
    public String reviewUpdate(@PathVariable("reviewId") Long reviewId, Model model, HttpSession session) {
        Review review = lectureService.getReview(reviewId);

        if (review.getMember().getId().equals((Long) session.getAttribute("memberId"))) {
            model.addAttribute("form", new ReviewUpdateForm(review.getContent()));
            return "lecture/review-update";
        }
        return "lecture/review-update-not-member";
    }

    @PostMapping("review/update/{reviewId}")
    public String reviewUpdate(@ModelAttribute("form") ReviewUpdateForm form, @PathVariable("reviewId") Long
            reviewId, HttpSession session) {
        Review review = lectureService.getReview(reviewId);

        if (review.getMember().getId().equals((Long) session.getAttribute("memberId"))) {
            lectureService.reviewUpdate(review.getId(), form);
            return "redirect:/lecture/review/read/" + review.getLecture().getId();
        }
        return "lecture/review-update-not-member";
    }

    @GetMapping("review/delete/{reviewId}")
    public String reviewDelete(@PathVariable("reviewId") Long reviewId, HttpSession session) {
        Review review = lectureService.getReview(reviewId);
        if (review.getMember().getId().equals((Long) session.getAttribute("memberId"))) {
            lectureService.reviewDelete(reviewId);
            return "redirect:/lecture/review/read/" + review.getLecture().getId();
        }
        return "lecture/review-update-not-member";
    }

    @GetMapping("review/teacherComment/write/{reviewId}")
    public String teacherCommentWrite(@PathVariable("reviewId") Long reviewId, HttpSession session, Model model) {
        boolean validTeacher = lectureService.validateTeacher((Long) session.getAttribute("teacherId"), reviewId);

        if (validTeacher) {
            model.addAttribute("form", new TeacherCommentWriteForm());
            return "lecture/teacherComment-write";
        } else {
            return "lecture/teacherComment-validTeacher-false";
        }
    }

    @PostMapping("review/teacherComment/write/{reviewId}")
    public String teacherCommentWrite(@Validated @ModelAttribute("form") TeacherCommentWriteForm
                                              form, BindingResult br,
                                      @PathVariable("reviewId") Long reviewId) {

        if (br.hasErrors())
            return "lecture/teacherComment-write";

        Long lectureId = lectureService.teacherCommentWrite(form.getContent(), reviewId);
        return "redirect:/lecture/review/read/" + lectureId;
    }

    @GetMapping("teacherComment/update/{teacherCommentId}")
    public String teacherCommentUpdate(@PathVariable("teacherCommentId") Long teacherCommentId, HttpSession
            session, Model model) {
        Long teacherId = (Long) session.getAttribute("teacherId");
        boolean validTeacher = lectureService.validateTeacherByTeacherCommentId(teacherCommentId, teacherId);

        if (validTeacher) {
            TeacherComment teacherComment = lectureService.getTeacherComment(teacherCommentId);

            model.addAttribute("form", new TeacherCommentUpdateForm(teacherComment.getContent()));
            return "lecture/teacherComment-update";
        } else {
            return "lecture/teacherComment-validTeacher-false";
        }
    }

    @PostMapping("teacherComment/update/{teacherCommentId}")
    public String teacherCommentUpdate(@Validated @ModelAttribute("form") TeacherCommentUpdateForm
                                               form, BindingResult br,
                                       @PathVariable("teacherCommentId") Long teacherCommentId, HttpSession session) {
        Long teacherId = (Long) session.getAttribute("teacherId");
        boolean validTeacher = lectureService.validateTeacherByTeacherCommentId(teacherCommentId, teacherId);

        if (validTeacher) {
            if (br.hasErrors())
                return "lecture/teacherComment-update";

            Long lectureId = lectureService.teacherCommentUpdate(teacherCommentId, form.getContent());
            return "redirect:/lecture/review/read/" + lectureId;
        } else {
            return "lecture/teacherComment-validTeacher-false";
        }
    }

    @GetMapping("teacherComment/delete/{teacherCommentId}")
    public String teacherCommentDelete(@PathVariable("teacherCommentId") Long teacherCommentId, HttpSession session) {
        Long teacherId = (Long) session.getAttribute("teacherId");
        boolean validTeacher = lectureService.validateTeacherByTeacherCommentId(teacherCommentId, teacherId);

        if (validTeacher) {
            Long lectureId = lectureService.deleteTeacherComment(teacherCommentId);
            return "redirect:/lecture/review/read/" + lectureId;
        } else {
            return "lecture/teacherComment-validTeacher-false";
        }
    }

    @PutMapping("video/watched/{videoId}")
    @ResponseBody
    public String videoWatched(@PathVariable("videoId")Long videoId,HttpSession session){
        Long memberId = (Long) session.getAttribute("memberId");
        if(memberId==null)
            return "not Member";
        Member member = memberService.info(memberId);
        Video video = lectureService.findVideo(videoId);
        Lecture lecture = video.getLecture();

        MemberLecture memberLecture = member.getLectures().stream()
                .filter(ml -> ml.getLecture().getId().equals(lecture.getId()))
                .findAny().orElse(null);

        if(memberLecture!=null){
            MemberLectureVideo memberLectureVideo = memberLecture.watchedVideosUpdate(video);
            lectureService.memberLectureVideoSave(memberLectureVideo);
        }


        log.info("videoId = {}",videoId);
        return "ok";
    }
}
