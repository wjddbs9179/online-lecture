package online.lecture.lecture.service;

import lombok.RequiredArgsConstructor;
import online.lecture.entity.Lecture;
import online.lecture.entity.Video;
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

    public Video nextVideo(Long lectureId, Long videoId) {
        return videoRepository.nextVideo(lectureId,videoId);
    }

    public Video prevVideo(Long lectureId, Long videoId) {
        return videoRepository.prevVideo(lectureId,videoId);
    }

    public List<Lecture> filter(String category) {
        return lectureRepository.filter(category);
    }
}
