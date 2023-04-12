package online.lecture.member.controller.domain;

import lombok.Data;

@Data
public class UpdateMemberForm {
    private String password;
    private String username;
    private String email;
}
