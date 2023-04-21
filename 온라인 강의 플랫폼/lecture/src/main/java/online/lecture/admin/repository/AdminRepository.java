package online.lecture.admin.repository;

import lombok.RequiredArgsConstructor;
import online.lecture.admin.controller.domain.AdminLoginForm;
import online.lecture.admin.controller.domain.AdminUpdateForm;
import online.lecture.entity.member.Admin;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class AdminRepository {

    private final EntityManager em;

    public void save(Admin admin){
        em.persist(admin);
    }

    public void delete(Admin admin){
        em.remove(admin);
    }

    public Admin find(Long id){
        return em.find(Admin.class,id);
    }

    public void update(Long id, AdminUpdateForm form){
        Admin admin = em.find(Admin.class, id);
        admin.update(form);
    }

    public Admin findByUserId(String userId) {
        return em.createQuery("select a from Admin a where userId=:userId",Admin.class)
                .setParameter("userId",userId)
                .getResultList().stream().findAny().orElse(null);
    }

    public Admin findByLoginForm(AdminLoginForm form) {
        return em.createQuery("select a from Admin a where userId=:userId and password=:password",Admin.class)
                .setParameter("userId",form.getUserId())
                .setParameter("password",form.getPassword())
                .getResultList().stream().findAny().orElse(null);
    }
}
