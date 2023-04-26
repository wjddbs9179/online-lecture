package online.lecture.entity;

import lombok.Getter;
import online.lecture.entity.member.Admin;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
@Entity
@Getter
public class AdminComment {

    @Id
    @GeneratedValue
    private Long id;

    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    protected AdminComment(){}

    public AdminComment(String content, Admin admin, Review review){
        this.content=content;
        this.admin=admin;
        this.review=review;
        review.getAdminComment().add(this);
    }

    public void update(String content) {
        this.content = content;
    }
}
