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

    public void save(Lecture lecture) {
        em.persist(lecture);
    }

    public Lecture find(Long id) {
        return em.createQuery("select l from Lecture l where l.id=:id", Lecture.class)
                .setParameter("id", id)
                .getResultList().stream().findAny().orElse(null);
    }

    public List<Lecture> filter(String category,String nameQuery,int page) {

        if(category.equals("")){
            return em.createQuery("select l from Lecture l where pub=true and l.name like concat('%',:nameQuery,'%')",Lecture.class)
                    .setParameter("nameQuery",nameQuery)
                    .setFirstResult((page-1)*9)
                    .setMaxResults(9)
                    .getResultList();
        }
        Category mainCategory = Arrays.stream(Category.values()).filter(c -> c.name().equals(category)).findAny().orElse(null);
        if (mainCategory != null) {
            return em.createQuery("select l from Lecture l where l.category=:category and pub=true and l.name like concat('%',:nameQuery,'%')",Lecture.class)
                    .setParameter("nameQuery",nameQuery)
                    .setParameter("category",mainCategory)
                    .setFirstResult((page-1)*9)
                    .setMaxResults(9)
                    .getResultList();
        }else {
            SubCategory subCategory = Arrays.stream(SubCategory.values()).filter(sc -> sc.name().equals(category)).findAny().orElse(null);
            return em.createQuery("select l from Lecture l where l.subCategory=:subCategory and pub=true and l.name like concat('%',:nameQuery,'%') ",Lecture.class)
                    .setParameter("nameQuery",nameQuery)
                    .setParameter("subCategory",subCategory)
                    .setFirstResult((page-1)*9)
                    .setMaxResults(9)
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

    public List<Video> getVideos(Long id) {
        return em.createQuery("select v from Video v where v.lecture.id=:id",Video.class)
                .setParameter("id",id)
                .getResultList();
    }

    public Long findIdByVideo(Long videoId) {
        Video video = em.createQuery("select v from Video v where v.id = :videoId", Video.class)
                .setParameter("videoId", videoId)
                .getResultList().stream().findAny().orElse(null);

        return video.getLecture().getId();
    }

    public Long findFirstVideoByLectureId(Lecture lecture) {
        return em.createQuery("select v from Video v where v.lecture=:lecture order by id",Video.class)
                .setParameter("lecture",lecture)
                .setMaxResults(1)
                .getResultList().stream().findAny().orElse(null).getId();

    }

    public Long getCount(String category, String nameQuery) {
        if(category.equals("")){
            return em.createQuery("select count(l) from Lecture l where pub=true and l.name like concat('%',:nameQuery,'%')",Long.class)
                    .setParameter("nameQuery",nameQuery)
                    .getResultList().stream().findAny().orElse(0L);
        }
        Category mainCategory = Arrays.stream(Category.values()).filter(c -> c.name().equals(category)).findAny().orElse(null);
        if (mainCategory != null) {
            return em.createQuery("select count(l) from Lecture l where l.category=:category and pub=true and l.name like concat('%',:nameQuery,'%')",Long.class)
                    .setParameter("nameQuery",nameQuery)
                    .setParameter("category",mainCategory)
                    .getResultList().stream().findAny().orElse(0L);
        }else {
            SubCategory subCategory = Arrays.stream(SubCategory.values()).filter(sc -> sc.name().equals(category)).findAny().orElse(null);
            return em.createQuery("select count(l) from Lecture l where l.subCategory=:subCategory and pub=true and l.name like concat('%',:nameQuery,'%') ",Long.class)
                    .setParameter("nameQuery",nameQuery)
                    .setParameter("subCategory",subCategory)
                    .getResultList().stream().findAny().orElse(0L);
        }
    }
}
