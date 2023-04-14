package online.lecture.lecture.entity;

public enum Category {
    DEVELOPMENT_PROGRAMMING("개발 · 프로그래밍"),
    SECURE_NETWORK("보안 · 네트워크"),
    DATA_SCIENCE("데이터 사이언스"),
    GAME_DEVELOPMENT("게임 개발"),
    HARDWARE("하드웨어"),
    CREATIVE("크리에이티브"),
    BUSINESS_MARKETING("비즈니스 · 마케팅"),
    STUDY_FOREIGN_LANGUAGE("학문 · 외국어"),
    CAREER("커리어"),
    SELF_DEVELOPMENT("자기계발");

    private String korName;
    Category(String korName){
        this.korName = korName;
    }

    public String getKorName() {
        return korName;
    }
}
