package online.lecture.member.repository;

import lombok.RequiredArgsConstructor;
import online.lecture.entity.Lecture;
import online.lecture.entity.Member;
import online.lecture.entity.MemberLecture;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberLectureRepository {
    private final EntityManager em;

    public List<Lecture> myLecture(Member member){
        List<MemberLecture> memberLectures = em.createQuery("select ml from MemberLecture ml join fetch" +
                        " ml.lecture where ml.member=:member", MemberLecture.class)
                .setParameter("member", member)
                .getResultList();
        List<Lecture> lectures = new ArrayList<>();
        for (MemberLecture memberLecture : memberLectures) {
            lectures.add(memberLecture.getLecture());
        }
        return lectures;
    }

}
