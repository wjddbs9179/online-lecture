package online.lecture.entity;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import static javax.persistence.FetchType.*;

@Entity
@Getter
public class Video {
    @Id @GeneratedValue
    @Column(name = "video_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    private Lecture lecture;

    private String videoRoute;

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public void setVideoRoute(String videoRoute) {
        this.videoRoute = videoRoute;
    }

    void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }
}
