package online.lecture.lecture.repository;

import lombok.RequiredArgsConstructor;
import online.lecture.entity.Lecture;
import online.lecture.entity.Video;
import online.lecture.entity.category.Category;
import online.lecture.entity.category.SubCategory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LectureRepository {

    private final EntityManager em;

    public List<Lecture> recentLecture() {
        return em.createQuery("select l from Lecture l where pub=true order by id desc", Lecture.class)
                .setMaxResults(9)
                .setFirstResult(0)
                .getResultList();
    }

    public void save(Lecture lecture) {
        em.persist(lecture);
    }

    public Lecture find(Long id) {
        return em.createQuery("select l from Lecture l join fetch l.videos where l.id=:id", Lecture.class)
                .setParameter("id", id)
                .getResultList().stream().findAny().orElse(null);
    }

    public List<Lecture> filter(String category) {

        Category mainCategory = Arrays.stream(Category.values()).filter(c -> c.name().equals(category)).findAny().orElse(null);

        if (mainCategory != null) {
            return em.createQuery("select l from Lecture l where category=:category and pub=true",Lecture.class)
                    .setParameter("category",mainCategory)
                    .getResultList();
        }else {
            SubCategory subCategory = Arrays.stream(SubCategory.values()).filter(sc -> sc.name().equals(category)).findAny().orElse(null);
            return em.createQuery("select l from Lecture l where subCategory=:subCategory and pub=true",Lecture.class)
                    .setParameter("subCategory",subCategory)
                    .getResultList();
        }

    }

    public List<Lecture> myLectureTeacher(Long teacherId) {
        return em.createQuery("select l from Lecture l where l.teacher.id=:teacherId",Lecture.class)
                .setParameter("teacherId",teacherId)
                .getResultList();
    }

    public List<Lecture> findByPubFalse() {
        return em.createQuery("select l from Lecture  l where l.pub=false",Lecture.class)
                .getResultList();
    }

    public void deleteById(Long id) {
        Lecture lecture = em.find(Lecture.class, id);
        em.createQuery("delete from Video v where v.lecture=:lecture")
                .setParameter("lecture",lecture)
                .executeUpdate();
        em.remove(lecture);
    }
}
