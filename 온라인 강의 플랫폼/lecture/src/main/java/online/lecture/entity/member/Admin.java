package online.lecture.entity.member;

import lombok.Getter;

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

}
