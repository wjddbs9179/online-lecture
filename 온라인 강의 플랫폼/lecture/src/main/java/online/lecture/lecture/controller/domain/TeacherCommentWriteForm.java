package online.lecture.lecture.controller.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TeacherCommentWriteForm {
    @NotBlank
    private String content;
}
