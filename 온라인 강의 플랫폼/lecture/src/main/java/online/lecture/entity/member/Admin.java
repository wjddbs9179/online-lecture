package online.lecture.entity.member;

import lombok.Getter;
import online.lecture.admin.controller.domain.AdminUpdateForm;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Admin {

    @Id @GeneratedValue
    private Long id;
    private String userId;
    private String password;
    private String username;
    private String email;

    public void update(AdminUpdateForm form) {
        this.userId = form.getUserId();
        this.password = form.getPassword();
        this.username = form.getUsername();
        this.email = form.getEmail();
    }
}
