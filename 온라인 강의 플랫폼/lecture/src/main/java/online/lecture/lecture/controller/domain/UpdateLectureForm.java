package online.lecture.lecture.controller.domain;

import lombok.Data;
import online.lecture.entity.Lecture;
import online.lecture.entity.Video;
import online.lecture.entity.category.Category;
import online.lecture.entity.category.SubCategory;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
public class UpdateLectureForm {

    @NotBlank(message = "강의명을 입력해주세요")
    private String name;

    @NotNull(message = "카테고리를 선택해주세요.")
    private Category category;

    private List<Video> videos = new ArrayList<>();

    private String imageRoute;

    private MultipartFile imageFile;

    private List<MultipartFile> videoFiles;

    private String intro;

    public static UpdateLectureForm createForm(Lecture lecture) {
        UpdateLectureForm form = new UpdateLectureForm();
        form.setName(lecture.getName());
        form.setCategory(lecture.getCategory());
        form.setIntro(lecture.getIntro());
        form.setVideos(lecture.getVideos());
        form.setImageRoute(lecture.getImageRoute());

        return form;
    }
}