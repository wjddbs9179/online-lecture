package online.lecture.member.controller.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class JoinMemberForm {
    @NotNull
    @NotBlank
    private String userId;
    @NotNull
    @NotBlank
    private String password1;
    @NotNull
    @NotBlank
    private String password2;
    @NotNull
    @NotBlank
    private String username;
    private String email;
}
