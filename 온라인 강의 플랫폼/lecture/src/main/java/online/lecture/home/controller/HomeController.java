package online.lecture.home.controller;

import lombok.RequiredArgsConstructor;
import online.lecture.lecture.entity.Lecture;
import online.lecture.lecture.service.LectureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @Value("${file.dir}")
    private String fileDir;

    private final LectureService lectureService;
    @RequestMapping("/")
    public String home(Model model){

        List<Lecture> recentLecture = lectureService.recentLecture();

        model.addAttribute("fileDir",fileDir);
        model.addAttribute("recentLecture",recentLecture);

        return "home";
    }
}
