package online.lecture.member.controller.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PwSearchForm {
    @NotBlank(message = "아이디를 입력해주세요.")
    private String userId;
    @NotBlank(message = "이름을 입력해주세요.")
    private String username;
    @NotBlank(message = "email을 입력해주세요.")
    private String email;
}
