package online.lecture.lecture.controller.domain;

import lombok.Data;

@Data
public class ReviewUpdateForm {

    private String content;

    public ReviewUpdateForm(String content) {
        this.content = content;
    }
}
