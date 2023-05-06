package online.lecture.lecture.repository;

import lombok.RequiredArgsConstructor;
import online.lecture.entity.Lecture;
import online.lecture.entity.Video;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class VideoRepository {

    private final EntityManager em;


    public void save(Video video){
        em.persist(video);
    }

    public void lectureUpdateVideo(List<Video> videos){
        for(Video video : videos){
            em.persist(em.merge(video));
        }
    }

    public Video find(Long id) {
        return em.createQuery("select v from Video v where v.id=:id",Video.class)
                .setParameter("id",id)
                .getResultList().stream().findAny().orElse(null);
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

    public void merge(Video video) {
        em.merge(video);
    }

    public void deleteVideoByLecture(Lecture lecture) {
        List<Video> videos = em.createQuery("select v from Video v where v.lecture=:lecture", Video.class)
                .setParameter("lecture", lecture)
                .getResultList();

        for(Video video : videos){
            em.createQuery("delete from MemberLectureVideo mlv where mlv.video=:video")
                    .setParameter("video",video)
                    .executeUpdate();
            em.remove(video);
        }
    }
}
