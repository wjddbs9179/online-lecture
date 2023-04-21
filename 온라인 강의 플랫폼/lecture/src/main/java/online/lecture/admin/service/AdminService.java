package online.lecture.admin.service;

import lombok.RequiredArgsConstructor;
import online.lecture.admin.controller.domain.AdminLoginForm;
import online.lecture.admin.controller.domain.AdminUpdateForm;
import online.lecture.admin.repository.AdminRepository;
import online.lecture.entity.Lecture;
import online.lecture.entity.member.Admin;
import online.lecture.lecture.repository.LectureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {
    private final AdminRepository adminRepository;

    private final LectureRepository lectureRepository;

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
}
