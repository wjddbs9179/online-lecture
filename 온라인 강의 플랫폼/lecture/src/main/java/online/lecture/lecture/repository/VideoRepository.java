package online.lecture.lecture.repository;

import lombok.RequiredArgsConstructor;
import online.lecture.entity.Lecture;
import online.lecture.entity.Video;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class VideoRepository {

    private final EntityManager em;

    public void save(Video video){
        em.persist(video);
    }

    public Video find(Long id) {
        return em.find(Video.class,id);
    }

    public Video nextVideo(Long lectureId, Long videoId) {
        Lecture lecture = em.find(Lecture.class, lectureId);
        return em.createQuery("select v from Video v where v.lecture=:lecture and id>:id",Video.class)
                .setParameter("lecture",lecture)
                .setParameter("id",videoId)
                .setMaxResults(1)
                .getResultList().stream().findAny().orElse(null);
    }

    public Video prevVideo(Long lectureId, Long videoId){
        Lecture lecture = em.find(Lecture.class,lectureId);
        return em.createQuery("select v from Video v where v.lecture=:lecture and id<:id order by id desc",Video.class)
                .setParameter("lecture",lecture)
                .setParameter("id",videoId)
                .setMaxResults(1)
                .getResultList().stream().findAny().orElse(null);
    }
}
