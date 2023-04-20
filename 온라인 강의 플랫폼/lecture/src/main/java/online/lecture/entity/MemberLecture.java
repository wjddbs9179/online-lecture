package online.lecture.entity;

import lombok.Getter;
import online.lecture.entity.member.Member;

import javax.persistence.*;

@Entity
@Getter
public class MemberLecture {
    @Id @GeneratedValue
    @Column(name = "member_lecture_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    public MemberLecture(Member member, Lecture lecture) {
        this.member = member;
        this.lecture = lecture;
    }

    public MemberLecture() {
    }
}
