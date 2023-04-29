package online.lecture.member.repository;

import lombok.RequiredArgsConstructor;
import online.lecture.entity.Lecture;
import online.lecture.entity.member.Member;
import online.lecture.entity.MemberLecture;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberLectureRepository {
    private final EntityManager em;

    public List<MemberLecture> myLecture(Member member){
        List<MemberLecture> memberLectures = em.createQuery("select ml from MemberLecture ml join fetch" +
                        " ml.lecture where ml.member=:member", MemberLecture.class)
                .setParameter("member", member)
                .getResultList();
        return memberLectures;
    }

    public MemberLecture findById(Long id) {
        return em.find(MemberLecture.class,id);
    }

    public void progressRateUpdate(double progressRate,Long memberLectureId) {
        em.createQuery("update MemberLecture ml set ml.progressRate=:progressRate where ml.id=:id")
                .setParameter("progressRate",(double)Math.round(progressRate*10000)/10000)
                .setParameter("id",memberLectureId)
                .executeUpdate();
    }

    public MemberLecture findByMemberAndLecture(Long memberId, Long lectureId) {
        return em.createQuery("select ml from MemberLecture ml where ml.member.id=:memberId and ml.lecture.id=:lectureId",MemberLecture.class)
                .setParameter("memberId",memberId)
                .setParameter("lectureId",lectureId)
                .getResultList().stream().findAny().orElse(null);
    }
}
