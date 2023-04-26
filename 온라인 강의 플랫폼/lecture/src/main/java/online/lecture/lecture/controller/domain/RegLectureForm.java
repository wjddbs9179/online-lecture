package online.lecture.lecture.controller.domain;

import lombok.Data;
import online.lecture.entity.category.Category;
import online.lecture.entity.Video;
import online.lecture.entity.category.SubCategory;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class RegLectureForm {

    @NotBlank(message = "강의명을 입력해주세요")
    private String name;

    @NotNull(message = "카테고리를 선택해주세요.")
    private Category category;

    private List<Video> videos = new ArrayList<>();

    private List<MultipartFile> imageFiles;
    @NotNull(message = "메인 이미지를 선택해주세요.")
    private MultipartFile attachFile;

    private List<MultipartFile> videoFiles;

    private String intro;
}
