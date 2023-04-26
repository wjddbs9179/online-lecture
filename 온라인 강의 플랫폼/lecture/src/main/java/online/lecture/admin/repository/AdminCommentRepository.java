package online.lecture.admin.repository;

import lombok.RequiredArgsConstructor;
import online.lecture.entity.AdminComment;
import online.lecture.entity.TeacherComment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AdminCommentRepository {
    private final EntityManager em;

    public AdminComment findById(Long adminCommentId) {
        return em.find(AdminComment.class,adminCommentId);
    }

    public AdminComment findByIdWithLecture(Long adminCommentId) {
        return em.createQuery("select ac from AdminComment ac " +
                        "join fetch ac.review r join fetch r.lecture where ac.id=:id",AdminComment.class)
                .setParameter("id",adminCommentId)
                .getResultList().stream().findAny().orElse(null);
    }

    public void delete(AdminComment adminComment) {
        em.remove(adminComment);
    }

    public List<AdminComment> findByLectureId(Long lectureId) {
        return em.createQuery("select ac from AdminComment ac where ac.review.lecture.id=:lectureId",AdminComment.class)
                .setParameter("lectureId",lectureId)
                .getResultList();
    }

    public void save(AdminComment adminComment) {
        em.persist(adminComment);
    }
}
