package online.lecture.lecture.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.lecture.entity.Lecture;
import online.lecture.entity.Review;
import online.lecture.entity.Video;
import online.lecture.lecture.repository.LectureRepository;
import online.lecture.lecture.repository.ReviewRepository;
import online.lecture.lecture.repository.VideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LectureService {

    private final LectureRepository lectureRepository;
    private final VideoRepository videoRepository;
    private final ReviewRepository reviewRepository;


    public void regLecture(Lecture lecture, List<Video> videos) {
        lectureRepository.save(lecture);

        for(Video video : videos){
            videoRepository.save(video);
        }
    }

    public Video findVideo(Long id){
        Video video = videoRepository.find(id);
        if(video.getLecture().getTeacher()!=null){
            log.info("teacher={}",video.getLecture().getTeacher().getId());
        }
        return video;
    }

    public List<Lecture> recentLecture() {
        return lectureRepository.recentLecture();
    }

    public Lecture info(Long id) {
        Lecture lecture = lectureRepository.find(id);
        log.info("teacher={}",lecture.getTeacher());
        return lecture;
    }

    public Video nextVideo(Long lectureId, Long videoId) {
        Video video = videoRepository.nextVideo(lectureId, videoId);
        if(video!=null) {
            if (video.getLecture().getTeacher() != null)
                log.info("teacher={}", video.getLecture().getTeacher().getUsername());
        }
        return video;
    }

    public Video prevVideo(Long lectureId, Long videoId) {
        Video video = videoRepository.prevVideo(lectureId, videoId);
        if(video!=null) {
            if (video.getLecture().getTeacher() != null)
                log.info("teacher={}", video.getLecture().getTeacher().getUsername());
        }
        return video;
    }

    public List<Lecture> filter(String category) {
        return lectureRepository.filter(category);
    }

    public void reviewWrite(Review review) {
        reviewRepository.save(review);
    }

    public List<Review> findReviews(Long lectureId) {
        return reviewRepository.findByLecture(lectureId);
    }
}
