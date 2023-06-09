package online.lecture.member.controller.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class JoinMemberForm {
    @NotNull
    @NotBlank(message = "아이디를 입력해주세요.")
    private String userId;
    @NotNull
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password1;
    private String password2;
    @NotNull
    @NotBlank(message = "이름을 입력해주세요.")
    private String username;
    @NotNull
    @NotBlank(message = "email을 입력해주세요.")
    private String email;
}
