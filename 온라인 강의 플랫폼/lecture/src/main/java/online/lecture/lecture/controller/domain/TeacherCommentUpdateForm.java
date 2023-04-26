package online.lecture.lecture.controller.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TeacherCommentUpdateForm {
    @NotBlank
    private String content;

    public TeacherCommentUpdateForm(String content) {
        this.content = content;
    }
}
