package online.lecture.member.repository;

import lombok.RequiredArgsConstructor;
import online.lecture.entity.member.Teacher;
import online.lecture.member.controller.domain.IdSearchForm;
import online.lecture.member.controller.domain.PwSearchForm;
import online.lecture.member.controller.domain.UpdateMemberForm;
import online.lecture.entity.member.Member;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

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

    public boolean updateTeacher(Long teacherId, UpdateMemberForm form) {
        Teacher teacher = em.find(Teacher.class, teacherId);
        try {
            teacher.update(form);
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
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTeacher(Teacher teacher) {
        try {
            em.remove(teacher);
            return true;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    public Member login(String userId, String password) {
        return em.createQuery("select m from Member m where userId=:userId and password=:password", Member.class)
                .setParameter("userId", userId)
                .setParameter("password", password)
                .getResultList().stream().findAny().orElse(null);
    }

    public Teacher loginTeacher(String userId, String password){
        return em.createQuery("select t from Teacher t where userId=:userId and password=:password",Teacher.class)
                .setParameter("userId",userId)
                .setParameter("password",password)
                .getResultList().stream().findAny().orElse(null);
    }

    public Member idSearch(IdSearchForm form) {
        return em.createQuery("select m from Member m where username=:username and email=:email", Member.class)
                .setParameter("username", form.getUsername())
                .setParameter("email", form.getEmail())
                .getResultList().stream().findAny().orElse(null);
    }

    public Member pwSearch(PwSearchForm form) {
        return em.createQuery("select m from Member m where userId=:userId and username=:username and email=:email", Member.class)
                .setParameter("userId", form.getUserId())
                .setParameter("username", form.getUsername())
                .setParameter("email", form.getEmail())
                .getResultList().stream().findAny().orElse(null);
    }

    public boolean findByUserId(String userId, String division) {
        if (division.equals("basic")) {
            Member member = em.createQuery("select m from Member m where userId=:userId", Member.class)
                    .setParameter("userId", userId)
                    .getResultList().stream().findAny().orElse(null);
            if (member == null)
                return true;
        }

        if(division.equals("teacher")){
            Teacher teacher = em.createQuery("select t from Teacher t where userId=:userId",Teacher.class)
                    .setParameter("userId",userId)
                    .getResultList().stream().findAny().orElse(null);
            if(teacher==null)
                return true;
        }

        return false;
    }

    public Long saveTeacher(Teacher teacher) {
        em.persist(teacher);
        return teacher.getId();
    }

    public Teacher infoTeacher(Long id) {
        return em.find(Teacher.class,id);
    }


}
