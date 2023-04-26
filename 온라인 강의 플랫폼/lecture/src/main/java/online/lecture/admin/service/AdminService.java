package online.lecture.admin.service;

import lombok.RequiredArgsConstructor;
import online.lecture.admin.controller.domain.AdminLoginForm;
import online.lecture.admin.controller.domain.AdminUpdateForm;
import online.lecture.admin.repository.AdminCommentRepository;
import online.lecture.admin.repository.AdminRepository;
import online.lecture.entity.AdminComment;
import online.lecture.entity.Lecture;
import online.lecture.entity.Review;
import online.lecture.entity.TeacherComment;
import online.lecture.entity.member.Admin;
import online.lecture.lecture.repository.LectureRepository;
import online.lecture.lecture.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {
    private final AdminRepository adminRepository;
    private final AdminCommentRepository adminCommentRepository;
    private final LectureRepository lectureRepository;
    private final ReviewRepository reviewRepository;

    public void postAdminJoin() {
        Admin admin = adminRepository.findByUserId("admin");

        if(admin==null) {
            admin = new Admin();
            AdminUpdateForm form = new AdminUpdateForm();
            form.setUserId("admin");
            form.setUsername("admin");
            form.setEmail("admin@admin.com");
            form.setPassword("1234");
            admin.update(form);
            adminRepository.save(admin);
        }
    }

    @Transactional(readOnly = true)
    public Admin login(AdminLoginForm form) {
        return adminRepository.findByLoginForm(form);
    }

    public List<Lecture> getWaitingLecture() {
        return lectureRepository.findByPubFalse();
    }

    public void pubTrue(Long id) {
        Lecture lecture = lectureRepository.find(id);
        lecture.setPub(true);
    }

    public void deleteLecture(Long id) {
        lectureRepository.deleteById(id);
    }

    public void pubFalse(Long id) {
        Lecture lecture = lectureRepository.find(id);
        lecture.setPub(false);
    }

    @Transactional(readOnly = true)
    public AdminComment getAdminComment(Long adminCommentId) {
        return adminCommentRepository.findById(adminCommentId);
    }

    public Long adminCommentUpdate(Long adminCommentId, String content) {
        AdminComment adminComment = adminCommentRepository.findByIdWithLecture(adminCommentId);
        adminComment.update(content);

        return adminComment.getReview().getLecture().getId();
    }

    public Long deleteAdminComment(Long adminCommentId) {
        AdminComment adminComment = adminCommentRepository.findByIdWithLecture(adminCommentId);
        Long lectureId = adminComment.getReview().getLecture().getId();

        adminCommentRepository.delete(adminComment);
        return lectureId;
    }

    public List<AdminComment> getAdminComments(Long lectureId) {
        return adminCommentRepository.findByLectureId(lectureId);
    }

    public Long adminCommentWrite(String content, Long reviewId,Long adminId) {
        Review review = reviewRepository.find(reviewId);
        AdminComment adminComment = new AdminComment(content, adminRepository.find(adminId), review);

        adminCommentRepository.save(adminComment);
        return review.getLecture().getId();
    }
}
