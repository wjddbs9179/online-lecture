package online.lecture.lecture.repository;

import lombok.RequiredArgsConstructor;
import online.lecture.entity.Review;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewRepository {

    private final EntityManager em;

    public void save(Review review){
        em.persist(review);
    }

    public List<Review> findByLecture(Long lectureId) {
        return em.createQuery("select r from Review r join fetch r.member where r.lecture.id=:lectureId",Review.class)
                .setParameter("lectureId",lectureId)
                .getResultList();
    }
}
