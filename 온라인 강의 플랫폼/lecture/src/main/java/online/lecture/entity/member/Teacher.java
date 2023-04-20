package online.lecture.entity.member;

import lombok.Getter;
import online.lecture.entity.Lecture;
import online.lecture.member.controller.domain.UpdateMemberForm;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Teacher {

    @Id @GeneratedValue
    private Long id;
    private String userId;
    private String username;
    private String password;
    private String email;

    @OneToMany(mappedBy = "teacher")
    private List<Lecture> myLectures = new ArrayList<>();

    public Teacher(String userId, String username, String password, String email) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Teacher() {

    }

    public void update(UpdateMemberForm form) {
        username = form.getUsername();
        password = form.getPassword();
        email = form.getEmail();
    }
}
