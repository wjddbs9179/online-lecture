package online.lecture.lecture.controller.domain;

import lombok.Data;
import online.lecture.entity.category.Category;
import online.lecture.entity.Video;
import online.lecture.entity.category.SubCategory;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class RegLectureForm {

    private String name;

    private Category category;

    private List<Video> videos = new ArrayList<>();

    private List<MultipartFile> imageFiles;
    private MultipartFile attachFile;

    private List<MultipartFile> videoFiles;

    private String intro;
}
