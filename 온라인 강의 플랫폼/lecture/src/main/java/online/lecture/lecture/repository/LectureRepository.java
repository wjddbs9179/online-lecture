package online.lecture.lecture.repository;

import lombok.RequiredArgsConstructor;
import online.lecture.lecture.entity.Lecture;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LectureRepository {

    private final EntityManager em;

    public List<Lecture> recentLecture() {
        return em.createQuery("select l from Lecture l",Lecture.class)
                .setMaxResults(5)
                .setFirstResult(0)
                .getResultList();
    }

    public void save(Lecture lecture){
        em.persist(lecture);
    }

    public Lecture find(Long id) {
        return em.find(Lecture.class,id);
    }
}
