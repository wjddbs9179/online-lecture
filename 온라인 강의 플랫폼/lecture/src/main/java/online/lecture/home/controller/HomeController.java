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
    public String homeFilter(Model model, @RequestParam(value = "category", defaultValue = "") String category,@RequestParam(value = "subCategory", defaultValue = "")String subCategory, @RequestParam(value = "nameQuery", defaultValue = "") String nameQuery
            , @RequestParam(value = "page", defaultValue = "1") int page) {
        List<Lecture> recentLecture = lectureService.filter(category, subCategory, nameQuery, page);
        Long count = lectureService.getLectureCount(category,nameQuery);
        int maxResult = 15;

        Category realCategory = Arrays.stream(Category.values()).filter(c -> c.name().equals(category)).findAny().orElse(null);
        SubCategory realSubCategory = Arrays.stream(SubCategory.values()).filter(c -> c.name().equals(category)).findAny().orElse(null);
        if (realCategory != null) {
            model.addAttribute("nowCategory", realCategory);
        } else if(realSubCategory!=null) {
            model.addAttribute("nowCategory", realSubCategory);
        } else {
            model.addAttribute("nowCategory", "");
        }

        model.addAttribute("firstPage",page%10==0 ? ((page-1)/10*10)+1 : ((page/10*10)+1));
        model.addAttribute("lastPage",page%10==0 ? ((page-1)/10+1)*10 : (page/10+1)*10);
        model.addAttribute("totalPage", count%maxResult==0 ? count/maxResult : count/maxResult+1);
        model.addAttribute("count",count);
        model.addAttribute("nameQuery",nameQuery);
        model.addAttribute("page", page);
        model.addAttribute("fileDir", fileDir);
        model.addAttribute("recentLecture", recentLecture);
        return "home";
    }
}
