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

    public List<Lecture> filter(String category, String subCategory, String nameQuery, int page) {

        int maxResult = 15;

        if (category.equals("")) {
            return em.createQuery("select l from Lecture l where pub=true and l.name like concat('%',:nameQuery,'%') order by l.id desc", Lecture.class)
                    .setParameter("nameQuery", nameQuery)
                    .setFirstResult((page - 1) * maxResult)
                    .setMaxResults(maxResult)
                    .getResultList();
        } else {
            Category mainCategory = Arrays.stream(Category.values()).filter(c -> c.name().equals(category)).findAny().orElse(null);
            SubCategory realSubCategory = Arrays.stream(SubCategory.values()).filter(sc -> sc.name().equals(subCategory)).findAny().orElse(null);
            if (realSubCategory == null) {
                return em.createQuery("select l from Lecture l where l.category=:category and pub=true and l.name like concat('%',:nameQuery,'%') order by l.id desc", Lecture.class)
                        .setParameter("nameQuery", nameQuery)
                        .setParameter("category", mainCategory)
                        .setFirstResult((page - 1) * maxResult)
                        .setMaxResults(maxResult)
                        .getResultList();
            } else {
                return em.createQuery("select l from Lecture l where l.category=:category and l.subCategory=:subCategory and pub=true and l.name like concat('%',:nameQuery,'%') order by l.id desc", Lecture.class)
                        .setParameter("nameQuery", nameQuery)
                        .setParameter("category", mainCategory)
                        .setParameter("subCategory", realSubCategory)
                        .setFirstResult((page - 1) * maxResult)
                        .setMaxResults(maxResult)
                        .getResultList();
            }
        }
    }

    public List<Lecture> myLectureTeacher(Long teacherId) {
        return em.createQuery("select l from Lecture l where l.teacher.id=:teacherId", Lecture.class)
                .setParameter("teacherId", teacherId)
                .getResultList();
    }

    public List<Lecture> findByPubFalse() {
        return em.createQuery("select l from Lecture  l where l.pub=false", Lecture.class)
                .getResultList();
    }

    public void deleteById(Long id) {
        Lecture lecture = em.find(Lecture.class, id);
        em.createQuery("delete from Video v where v.lecture=:lecture")
                .setParameter("lecture", lecture)
                .executeUpdate();
        em.remove(lecture);
    }

    public List<Video> getVideos(Long id) {
        return em.createQuery("select v from Video v where v.lecture.id=:id", Video.class)
                .setParameter("id", id)
                .getResultList();
    }

    public Long findIdByVideo(Long videoId) {
        Video video = em.createQuery("select v from Video v where v.id = :videoId", Video.class)
                .setParameter("videoId", videoId)
                .getResultList().stream().findAny().orElse(null);

        return video.getLecture().getId();
    }

    public Long findFirstVideoByLectureId(Lecture lecture) {
        return em.createQuery("select v from Video v where v.lecture=:lecture order by id", Video.class)
                .setParameter("lecture", lecture)
                .setMaxResults(1)
                .getResultList().stream().findAny().orElse(null).getId();

    }

    public Long getCount(String category, String nameQuery) {
        if (category.equals("")) {
            return em.createQuery("select count(l) from Lecture l where pub=true and l.name like concat('%',:nameQuery,'%')", Long.class)
                    .setParameter("nameQuery", nameQuery)
                    .getResultList().stream().findAny().orElse(0L);
        }
        Category mainCategory = Arrays.stream(Category.values()).filter(c -> c.name().equals(category)).findAny().orElse(null);
        if (mainCategory != null) {
            return em.createQuery("select count(l) from Lecture l where l.category=:category and pub=true and l.name like concat('%',:nameQuery,'%')", Long.class)
                    .setParameter("nameQuery", nameQuery)
                    .setParameter("category", mainCategory)
                    .getResultList().stream().findAny().orElse(0L);
        } else {
            SubCategory subCategory = Arrays.stream(SubCategory.values()).filter(sc -> sc.name().equals(category)).findAny().orElse(null);
            return em.createQuery("select count(l) from Lecture l where l.subCategory=:subCategory and pub=true and l.name like concat('%',:nameQuery,'%') ", Long.class)
                    .setParameter("nameQuery", nameQuery)
                    .setParameter("subCategory", subCategory)
                    .getResultList().stream().findAny().orElse(0L);
        }
    }

    public void sampleDataCreate() {
        String[] videoNames = {"학습안내(SQL, DB, DBMS의 의미와 필요성)"
                , "오라클 DBMS 18c XE 설치하기"
                , "SQL Developer 설치하기"
                , "오라클 PDB 서버에 접속하기"
                , "#03. 검색 엔진 최적화에도 중요한 h(n) 태그 - 프론트엔드 개발자 입문편 "
                , "[Node.js] 백엔드 맛보기 | http로 서버 띄워보기"
                , "Blazor Server 5.0 강의 소스인 Hawaso 솔루션을 닷넷 6로 업그레이드하기"
                , "키보드 마우스로 이동시켜보자! [유니티 입문 강좌 B6]"
                , "알고리즘 시간복잡도 2"
                , "알고리즘 시간복잡도 BigO"
                , "네트워크(기본+보안)강의 7주차"
                , "크롬 개발자 도구 2 - 엘리먼츠"
                , "정보보안의 요소 - 무결성 [자바(Java)로 이해하는 블록체인 이론과 실습 3강]"
                , "[데이터 분석 기초 강의] 3강. API를 사용하여 데이터 수집하기"};
        String[] videoRoutes = {"https://youtu.be/pGlkIFrY9QY"
                , "https://youtu.be/aDTiSKcMtoc"
                , "https://youtu.be/sOFZv_jEk_8"
                , "https://youtu.be/jzPGKAU4fCU"
                , "https://youtu.be/vKd6V0cXoL4"
                , "https://youtu.be/7rpwgjNuOwo"
                , "https://youtu.be/be6pyYAoFJE"
                , "https://youtu.be/drCj2k50j_k"
                , "https://youtu.be/FZuQ7DyhRfQ"
                , "https://youtu.be/sDWFWCvbrGg"
                , "https://youtu.be/kTFh3v3qoxA"
                , "https://youtu.be/qfGbsEdMaV4"
                , "https://youtu.be/GpHOvj3XVBU"
                , "https://youtu.be/mjyygG7zp1c"
                , "https://youtu.be/jp7vtbLin-s"
                , "https://youtu.be/sF4wwzlj_yQ"};

        String[] lectureNames = {"네트워크 보안 강의"
                , "구글 크롬 개발자 도구 사용설명서"
                , "네트워크 강의 기본편"
                , "쉽게 설명하는 AWS 기초 강좌"
                , "자바(Java)로 이해하는 블록체인 이론과 실습"
                , "유니티 기초 강좌"
                , "안드로이드개발 시작하기"
                , "[3] Blazor(블레이저) 풀스택 웹개발 무료 강의"
                , "[Node.js] 백엔드 맛보기"
                , "HTML,CSS 기초 강의(강좌)"
                , "Servlet/JSP 프로그래밍"};

        String[] lectureImageRoute = {"ad641a7e-5f2d-4cd9-abae-fc2bb5bfe909.png"
                , "1cabd34d-2445-466c-94b8-a3efae4e1332.png"
                , "564e5341-ccc1-4a5b-bd4d-4282561bef3b.png"
                , "2e9e5661-06f7-4847-b41c-a9d756ad7646.png"
                , "7708b90b-238d-4247-84ba-bbe2c353e66c.png"
                , "81fb025c-9a5f-4d82-9727-0cefaefd6ddd.png"
                , "b8d4990a-3ec6-4bde-91cb-2125ac747506.png"
                , "4fa54506-1eb5-4a1e-b35b-59276a1b4d18.png"
                , "03eac5d7-ae8f-48d7-9a7c-dd6e79c23f61.png"
                , "b300c984-8910-4197-badd-f8f9142e2588.png"
                , "2cbc2e2e-83a8-403a-b42d-60a6fc20ff88.png"
                , "9fbf1bee-f1b4-4728-abd2-2e8ca85918c7.png"
                , "355059c6-8a76-4c31-9208-f354cbc92203.png"
                , "7076452c-e74e-42db-94f7-a8ab719eb3f0.png"
                , "63477175-a6a4-450f-a53d-205746ce11d3.png"
                , "412bdc26-be9a-4e48-8efb-e5ccbe9055df.png"
                , "402d17fa-def2-4fa2-8432-92007c4c9646.png"};

        String[] intros = {"자바 강의",
                "오라클 데이터베이스 SQL 강의"
                , "Servlet/JSP 프로그래밍"
                , "HTML,CSS 기초 강의(강좌) - 프론트엔드 개발자 입문편"
                , "안녕하세요 구독자 여러분 :)\n" +
                "\n" +
                "주문해주셨던 풀스택을 다뤄보는 영상을 준비해봤습니다.\n" +
                "\n" +
                "메인은 백엔드에 맞춰 진행될 것이구요. 그러나 프런트에서는 무엇을 어떻게 하는지도 다룰 것이니 믿고 따라와 주세요 \uD83D\uDE0A"
                , "[3] Blazor(블레이저) 풀스택 웹개발 무료 강의"
                , "안드로이드 개발 강의"
                , "유니티를 이용한 게임 개발 강의"
                , "자료구조 강의"
                , "네트워크, 보안 강의"
                , "앱개발 강의"
                , "개발자 도구 강의"
                , "네트워크 강의"
                , "박성업 정보보안기사 필기 제1과목 시스템보안(1강)"
                , "클라우드 강의"
                , "자바(Java)로 이해하는 블록체인 이론과 실습"
                , "데이터 분석 강의"};

        for (int i = 0; i < 200; i++) {

            Category category = Category.values()[(int) (Math.random() * Category.values().length)];
            SubCategory subCategory = category.getSubCategories().get((int) (Math.random() * category.getSubCategories().size()));
            Lecture lecture = new Lecture(
                    lectureNames[(int) (Math.random() * lectureNames.length)]
                    , category
                    , subCategory
                    , lectureImageRoute[(int) (Math.random() * lectureImageRoute.length)]
                    , intros[(int) (Math.random() * intros.length)]
                    , null);
            lecture.setPub(true);
            em.persist(lecture);

            for (int j = 0; j < videoNames.length; j++) {
                Video video = new Video();
                video.setVideoRoute(videoRoutes[(int) (Math.random() * videoRoutes.length)]);
                video.setName(videoNames[(int) (Math.random() * videoNames.length)]);
                video.setLecture(lecture);
                em.persist(video);
            }
        }
    }
}
