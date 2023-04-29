package online.lecture.entity;

import lombok.Getter;
import online.lecture.entity.member.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "memberLecture")
    private List<MemberLectureVideo> watchedVideos = new ArrayList<>();

    private double progressRate;

    private Long LastWatchedVideoId;
    private int LastWatchedVideoTime;

    public void setLastWatchedVideoId(Long lastWatchedVideoId) {
        LastWatchedVideoId = lastWatchedVideoId;
    }

    public void setLastWatchedVideoTime(int lastWatchedVideoTime) {
        LastWatchedVideoTime = lastWatchedVideoTime;
    }

    public void setProgressRate(double progressRate) {
        this.progressRate = progressRate;
    }

    public MemberLecture(Member member, Lecture lecture) {
        this.member = member;
        this.lecture = lecture;
    }

    public MemberLectureVideo watchedVideosUpdate(Video video){
        MemberLectureVideo memberLectureVideo = new MemberLectureVideo(this, video);
        watchedVideos.add(memberLectureVideo);
        return memberLectureVideo;
    }

    public MemberLecture() {
    }
}
