package online.lecture.entity;

import lombok.Getter;
import online.lecture.entity.category.Category;
import online.lecture.entity.category.SubCategory;

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

    @Enumerated(EnumType.STRING)
    private SubCategory subCategory;

    @OneToMany(mappedBy = "lecture")
    private List<Video> videos = new ArrayList<>();

    private String intro;

    @OneToMany(mappedBy = "lecture")
    private List<MemberLecture> members = new ArrayList<>();


    public Lecture(String name, Category category, SubCategory subCategory, String imageRoute,String intro) {
        this.name = name;
        this.category = category;
        this.subCategory = subCategory;
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
