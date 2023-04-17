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
        return em.createQuery("select l from Lecture l order by id desc",Lecture.class)
                .setMaxResults(9)
                .setFirstResult(0)
                .getResultList();
    }

    public void save(Lecture lecture){
        em.persist(lecture);
    }

    public Lecture find(Long id) {
        return em.createQuery("select l from Lecture l join fetch l.videos where l.id=:id", Lecture.class)
                .setParameter("id",id)
                .getResultList().stream().findAny().orElse(null);
    }
}
