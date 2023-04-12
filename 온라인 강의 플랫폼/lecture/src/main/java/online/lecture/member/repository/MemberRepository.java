package online.lecture.member.repository;

import lombok.RequiredArgsConstructor;
import online.lecture.member.controller.domain.UpdateMemberForm;
import online.lecture.member.entity.Member;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    public boolean update(Long id, UpdateMemberForm form) {
        Member member = em.find(Member.class, id);
        try {
            member.update(form);
            return true;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(Member member) {
        try {
            em.remove(member);
            return true;
        }catch(IllegalArgumentException e){
            e.printStackTrace();
            return false;
        }
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    public Member login(String userId, String password){
        return em.createQuery("select m from Member m where userId=:userId and password=:password",Member.class)
                .setParameter("userId",userId)
                .setParameter("password",password)
                .getResultList().stream().findAny().orElse(null);
    }
}
