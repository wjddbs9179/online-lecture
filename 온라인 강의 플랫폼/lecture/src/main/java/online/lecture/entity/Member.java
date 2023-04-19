package online.lecture.entity;

import lombok.Getter;
import lombok.ToString;
import online.lecture.entity.Lecture;
import online.lecture.member.controller.domain.UpdateMemberForm;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String userId;
    private String username;
    private String password;
    private String email;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberLecture> lectures = new ArrayList<>();

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

    public void addLecture(MemberLecture memberLecture) {
        lectures.add(memberLecture);
    }
}
