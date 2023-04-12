package online.lecture.member.controller.domain;

import lombok.Data;

@Data
public class LoginMemberForm {
    private String userId;
    private String password;
}
