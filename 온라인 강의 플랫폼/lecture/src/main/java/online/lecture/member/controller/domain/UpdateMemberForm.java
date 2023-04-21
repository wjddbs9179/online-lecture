package online.lecture.member.controller.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateMemberForm {
    @NotBlank(message = "비밀번호는 필수입니다.")
    @NotNull
    private String password;
    @NotBlank(message = "이름은 필수입니다.")
    @NotNull
    private String username;
    @NotBlank(message = "email은 필수입니다.")
    @NotNull
    private String email;
}
