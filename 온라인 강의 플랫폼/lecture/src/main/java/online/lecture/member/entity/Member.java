package online.lecture.member.entity;

import lombok.Getter;
import lombok.ToString;
import online.lecture.member.controller.domain.UpdateMemberForm;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@ToString
public class Member {

    @Id @GeneratedValue
    private Long id;
    private String userId;
    private String username;
    private String password;
    private String email;

    protected Member(){

    }

    public Member(String userId, String username, String password, String email) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public void update(UpdateMemberForm form) {
        username = form.getUsername();
        password = form.getPassword();
        email = form.getEmail();
    }
}
