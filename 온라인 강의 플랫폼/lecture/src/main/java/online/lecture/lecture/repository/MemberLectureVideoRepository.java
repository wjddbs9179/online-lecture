package online.lecture.lecture.repository;

import lombok.RequiredArgsConstructor;
import online.lecture.entity.MemberLecture;
import online.lecture.entity.MemberLectureVideo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberLectureVideoRepository {
    private final EntityManager em;

    public MemberLectureVideo save(MemberLectureVideo memberLectureVideo){
        MemberLectureVideo findMemberLectureVideo = em.createQuery("select mlv from MemberLectureVideo mlv where mlv.memberLecture=:ml and mlv.video=:v", MemberLectureVideo.class)
                .setParameter("ml", memberLectureVideo.getMemberLecture())
                .setParameter("v", memberLectureVideo.getVideo())
                .getResultList().stream().findAny().orElse(null);
        if(findMemberLectureVideo==null) {
            em.persist(memberLectureVideo);
            em.flush();
            em.clear();
            return memberLectureVideo;
        }
        em.flush();
        em.clear();
        return findMemberLectureVideo;
    }

    public MemberLectureVideo find(Long id) {
        return em.createQuery("select mlv from MemberLectureVideo mlv join fetch mlv.memberLecture ml join fetch ml.lecture l", MemberLectureVideo.class)
                .getResultList().stream().findAny().orElse(null);
    }

    public List<MemberLectureVideo> getWatchedVideosByMemberLecture(MemberLecture memberLecture) {
        return em.createQuery("select mlv from MemberLectureVideo mlv where mlv.memberLecture=:memberLecture", MemberLectureVideo.class)
                .setParameter("memberLecture",memberLecture)
                .getResultList();
    }
}
