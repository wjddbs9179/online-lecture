package online.lecture.entity;

import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
public class MemberLectureVideo {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_lecture_id")
    private MemberLecture memberLecture;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "video_id")
    private Video video;


    public MemberLectureVideo() {
    }

    public MemberLectureVideo(MemberLecture memberLecture, Video video) {
        this.memberLecture = memberLecture;
        this.video = video;
    }
}
