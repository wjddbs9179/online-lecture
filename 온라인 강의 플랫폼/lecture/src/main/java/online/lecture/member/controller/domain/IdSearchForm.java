package online.lecture.member.controller.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class IdSearchForm {
    @NotNull
    @NotBlank(message = "이름을 입력해주세요.")
    private String username;
    @NotNull
    @NotBlank(message = "email을 입력해주세요.")
    private String email;
}
