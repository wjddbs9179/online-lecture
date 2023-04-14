package online.lecture.lecture.service;

import lombok.RequiredArgsConstructor;
import online.lecture.lecture.entity.Lecture;
import online.lecture.lecture.entity.Video;
import online.lecture.lecture.repository.LectureRepository;
import online.lecture.lecture.repository.VideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LectureService {

    private final LectureRepository lectureRepository;
    private final VideoRepository videoRepository;


    public void regLecture(Lecture lecture, List<Video> videos) {
        lectureRepository.save(lecture);

        for(Video video : videos){
            videoRepository.save(video);
        }
    }

    public Video findVideo(Long id){
        return videoRepository.find(id);
    }

    public List<Lecture> recentLecture() {
        return lectureRepository.recentLecture();
    }

    public Lecture info(Long id) {

        return lectureRepository.find(id);
    }
}
