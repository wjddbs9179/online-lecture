package online.lecture.home.controller;

import lombok.RequiredArgsConstructor;
import online.lecture.entity.Lecture;
import online.lecture.entity.category.Category;
import online.lecture.entity.category.SubCategory;
import online.lecture.lecture.service.LectureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @Value("${file.dir}")
    private String fileDir;

    private final LectureService lectureService;

    @GetMapping("/")
    public String homeFilter(Model model, @RequestParam(value = "category", defaultValue = "") String category, @RequestParam(value = "nameQuery", defaultValue = "") String nameQuery
            , @RequestParam(value = "page", defaultValue = "1") int page) {
        List<Lecture> recentLecture = lectureService.filter(category, nameQuery, page);
        Long count = lectureService.getLectureCount(category,nameQuery);


        Category realCategory = Arrays.stream(Category.values()).filter(c -> c.name().equals(category)).findAny().orElse(null);
        SubCategory realSubCategory = Arrays.stream(SubCategory.values()).filter(c -> c.name().equals(category)).findAny().orElse(null);
        if (realCategory != null) {
            model.addAttribute("nowCategory", realCategory);
        } else if(realSubCategory!=null) {
            model.addAttribute("nowCategory", realSubCategory);
        } else {
            model.addAttribute("nowCategory", "");
        }

        model.addAttribute("firstPage",page%5==0 ? ((page-1)/5*5)+1 : ((page/5*5)+1));
        model.addAttribute("lastPage",page%5==0 ? ((page-1)/5+1)*5 : (page/5+1)*5);
        model.addAttribute("totalPage", count%9==0 ? count/9 : count/9+1);
        model.addAttribute("count",count);
        model.addAttribute("nameQuery",nameQuery);
        model.addAttribute("page", page);
        model.addAttribute("fileDir", fileDir);
        model.addAttribute("recentLecture", recentLecture);
        return "home";
    }
}
