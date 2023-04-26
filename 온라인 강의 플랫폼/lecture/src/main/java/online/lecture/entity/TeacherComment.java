package online.lecture.entity;

import lombok.Getter;
import online.lecture.entity.member.Teacher;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
public class TeacherComment {
    @Id @GeneratedValue
    private Long id;

    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    protected TeacherComment(){}

    public TeacherComment(String content, Teacher teacher, Review review){
        this.content=content;
        this.teacher=teacher;
        this.review=review;
        review.getTeacherComment().add(this);
    }

    public void update(String content) {
        this.content = content;
    }
}
