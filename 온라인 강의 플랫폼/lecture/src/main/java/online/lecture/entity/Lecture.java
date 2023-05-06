package online.lecture.entity;

import lombok.Getter;
import online.lecture.entity.category.Category;
import online.lecture.entity.category.SubCategory;
import online.lecture.entity.member.Teacher;
import online.lecture.lecture.controller.domain.UpdateLectureForm;
import online.lecture.lecture.controller.file.FileStore;
import online.lecture.lecture.controller.file.UploadFile;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
@Getter
public class Lecture {

    @Id
    @GeneratedValue
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @OneToMany(mappedBy = "lecture", cascade = ALL)
    private List<Review> reviews = new ArrayList<>();

    private boolean pub;

    public void setPub(boolean pub) {
        this.pub = pub;
    }

    public Lecture(String name, Category category, SubCategory subCategory, String imageRoute, String intro, Teacher teacher) {
        this.name = name;
        this.category = category;
        this.subCategory = subCategory;
        this.imageRoute = imageRoute;
        this.intro = intro;
        this.teacher = teacher;
    }

    public Lecture() {

    }

    public void addVideo(Video video) {
        videos.add(video);
        video.setLecture(this);
    }

    public void update(UpdateLectureForm form,SubCategory subCategory) throws IOException {
        FileStore fileStore = new FileStore();
        this.name = form.getName();
        this.intro = form.getIntro();
        this.subCategory = subCategory;
        this.category = form.getCategory();
        if (!form.getImageFile().isEmpty()) {
            UploadFile imageFile = fileStore.storeFile(form.getImageFile());
            this.imageRoute = imageFile.getStoreFilename();
        }
    }
}
