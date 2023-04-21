package online.lecture.admin.controller.domain;

import lombok.Data;

@Data
public class AdminUpdateForm {

    private String userId;
    private String password;
    private String username;
    private String email;
}
