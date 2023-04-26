package online.lecture.entity;

import lombok.Getter;
import lombok.Setter;
import online.lecture.entity.member.Member;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Setter
public class Review {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String content;

    @OneToMany(mappedBy = "review",cascade = CascadeType.ALL)
    private List<TeacherComment> teacherComment = new ArrayList<>();


    @OneToMany(mappedBy = "review",cascade = CascadeType.ALL)
    private List<AdminComment> adminComment = new ArrayList<>();

    public void writeReview(Lecture lecture, Member member, String content){
        setLecture(lecture);
        setMember(member);
        lecture.getReviews().add(this);
        member.getReviews().add(this);
        setContent(content);
    }

}
