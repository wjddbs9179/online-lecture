package online.lecture.lecture.repository;

import lombok.RequiredArgsConstructor;
import online.lecture.entity.MemberLectureVideo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class MemberLectureVideoRepository {
    private final EntityManager em;

    public void save(MemberLectureVideo memberLectureVideo){
        MemberLectureVideo findMemberLectureVideo = em.createQuery("select mlv from MemberLectureVideo mlv where mlv.memberLecture=:ml and mlv.video=:v", MemberLectureVideo.class)
                .setParameter("ml", memberLectureVideo.getMemberLecture())
                .setParameter("v", memberLectureVideo.getVideo())
                .getResultList().stream().findAny().orElse(null);
        if(findMemberLectureVideo==null) {
            em.persist(memberLectureVideo);
        }
    }

    public MemberLectureVideo find(Long id) {
        return em.createQuery("select mlv from MemberLectureVideo mlv join fetch mlv.memberLecture ml join fetch ml.lecture l join fetch l.videos", MemberLectureVideo.class)
                .getResultList().stream().findAny().orElse(null);
    }
}
