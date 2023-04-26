package online.lecture.lecture.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.lecture.entity.*;
import online.lecture.lecture.controller.domain.ReviewUpdateForm;
import online.lecture.lecture.repository.*;
import online.lecture.member.repository.MemberLectureRepository;
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
    private final TeacherCommentRepository teacherCommentRepository;
    private final MemberLectureVideoRepository memberLectureVideoRepository;
    private final MemberLectureRepository memberLectureRepository;


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

    public Review getReview(Long reviewId) {
        return reviewRepository.find(reviewId);
    }

    public void reviewUpdate(Long id, ReviewUpdateForm form) {
        Review review = reviewRepository.find(id);
        review.setContent(form.getContent());
    }

    public void reviewDelete(Long reviewId) {
        reviewRepository.delete(reviewId);
    }

    public boolean validateTeacher(Long teacherId, Long reviewId) {
        Review review = reviewRepository.find(reviewId);
        if(review.getLecture().getTeacher().getId().equals(teacherId))
            return true;

        return false;
    }

    public Long teacherCommentWrite(String content, Long reviewId) {
        Review review = reviewRepository.find(reviewId);
        TeacherComment teacherComment = new TeacherComment(content, review.getLecture().getTeacher(), review);

        teacherCommentRepository.save(teacherComment);
        return review.getLecture().getId();
    }

    public List<TeacherComment> getTeacherComments(Long lectureId) {
        return teacherCommentRepository.findByLectureId(lectureId);
    }

    public boolean validateTeacherByTeacherCommentId(Long teacherCommentId, Long teacherId) {
        TeacherComment teacherComment = teacherCommentRepository.findById(teacherCommentId);

        if(teacherComment.getTeacher().getId().equals(teacherId))
            return true;

        return false;
    }

    public Long teacherCommentUpdate(Long teacherCommentId, String content) {
        TeacherComment teacherComment = teacherCommentRepository.findById(teacherCommentId);
        teacherComment.update(content);

        return teacherComment.getReview().getLecture().getId();
    }

    public TeacherComment getTeacherComment(Long teacherCommentId) {
        return teacherCommentRepository.findByIdOnlyTeacherComment(teacherCommentId);
    }

    public Long deleteTeacherComment(Long teacherCommentId) {
        TeacherComment teacherComment = teacherCommentRepository.findByIdWithLecture(teacherCommentId);
        Long lectureId = teacherComment.getReview().getLecture().getId();

        teacherCommentRepository.delete(teacherComment);
        return lectureId;
    }

    public void memberLectureVideoSave(MemberLectureVideo memberLectureVideo) {
        memberLectureVideoRepository.save(memberLectureVideo);
        MemberLectureVideo findMemberLectureVideo = memberLectureVideoRepository.find(memberLectureVideo.getId());
        MemberLecture memberLecture = findMemberLectureVideo.getMemberLecture();
        double watchedVideoSize = (double)memberLecture.getWatchedVideos().size();
        log.info("watchedVideoSize = {}",watchedVideoSize);
        double videosSize = (double) lectureRepository.getVideos(memberLecture.getLecture().getId()).size();
        log.info("videosSize = {}",videosSize);
        memberLecture.setProgressRate(watchedVideoSize/videosSize);
        log.info("memberLecture.progressRate = {}",memberLecture.getProgressRate());
    }
}
