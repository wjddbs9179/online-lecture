package online.lecture.lecture.repository;

import lombok.RequiredArgsConstructor;
import online.lecture.entity.TeacherComment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TeacherCommentRepository {

    private final EntityManager em;

    public void save(TeacherComment teacherComment){
        em.persist(teacherComment);
    }

    public List<TeacherComment> findByLectureId(Long lectureId) {
        return em.createQuery("select tc from TeacherComment tc where tc.review.lecture.id=:lectureId",TeacherComment.class)
                .setParameter("lectureId",lectureId)
                .getResultList();
    }

    public TeacherComment findById(Long teacherCommentId) {
        return em.createQuery("select tc from TeacherComment tc join fetch tc.teacher " +
                        "join fetch tc.review r " +
                        "join fetch r.lecture",TeacherComment.class)
                .getResultList().stream().findAny().orElse(null);
    }

    public TeacherComment findByIdOnlyTeacherComment(Long teacherCommentId) {
        return em.find(TeacherComment.class,teacherCommentId);
    }

    public TeacherComment findByIdWithLecture(Long teacherCommentId) {
        return em.createQuery("select tc from TeacherComment tc join fetch tc.review r join fetch r.lecture where tc.id=:id",TeacherComment.class)
                .setParameter("id",teacherCommentId)
                .getResultList().stream().findAny().orElse(null);
    }

    public void delete(TeacherComment teacherComment) {
        em.remove(teacherComment);
    }
}
