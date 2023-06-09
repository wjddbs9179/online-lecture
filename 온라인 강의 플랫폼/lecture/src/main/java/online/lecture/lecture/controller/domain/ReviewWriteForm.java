package online.lecture.lecture.controller.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ReviewWriteForm {
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
}
