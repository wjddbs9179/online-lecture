package online.lecture.member.entity;

import online.lecture.lecture.entity.Lecture;
import org.apache.ibatis.annotations.Many;

import javax.persistence.*;

@Entity
public class Member_Lecture {
    @Id @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;
}
