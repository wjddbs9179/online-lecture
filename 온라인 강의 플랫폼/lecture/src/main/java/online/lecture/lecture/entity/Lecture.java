package online.lecture.lecture.entity;

import lombok.Getter;
import online.lecture.lecture.controller.file.UploadFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Lecture {

    @Id @GeneratedValue
    @Column(name = "lecture_id")
    private Long id;

    private String name;

    private String imageRoute;

    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(mappedBy = "lecture")
    private List<Video> videos = new ArrayList<>();

    private String intro;


    public Lecture(String name, Category category, String imageRoute,String intro) {
        this.name = name;
        this.category = category;
        this.imageRoute = imageRoute;
        this.intro = intro;
    }

    public Lecture() {

    }

    public void addVideo(Video video){
        videos.add(video);
        video.setLecture(this);
    }
}
