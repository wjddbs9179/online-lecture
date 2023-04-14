package online.lecture.lecture.repository;

import lombok.RequiredArgsConstructor;
import online.lecture.lecture.entity.Video;
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
}
