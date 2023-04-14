package online.lecture.lecture.controller.domain;

import lombok.Data;
import online.lecture.lecture.entity.Category;
import online.lecture.lecture.entity.Video;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
public class RegLectureForm {

    private String name;

    private Category category;

    private List<Video> videos = new ArrayList<>();

    private List<MultipartFile> imageFiles;
    private MultipartFile attachFile;

    private String intro;
}
