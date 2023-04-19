package online.lecture.entity.category;

public enum SubCategory {
    WEB_DEVELOPMENT("웹 개발"),
    FRONT_END("프론트엔드"),
    BACK_END("백엔드"),
    FULL_STACK("풀스택"),
    MOBILE_APP_DEVELOPMENT("모바일 앱 개발"),
    GAME_DEVELOPMENT("게임 개발"),
    PROGRAMMING_LANGUAGE("프로그래밍 언어"),
    ALGORITHM_DATA_STRUCTURE("알고리즘 · 자료구조"),
    DATABASE("데이터베이스"),
    DEVOPS_INFRASTRUCTURE("데브옵스 · 인프라"),
    DEVELOPMENT_TOOL("개발 도구"),


    SECURITY("보안"),
    NETWORK("네트워크"),
    SYSTEM("시스템"),
    CLOUD("클라우드"),
    BLOCKCHAIN("블록체인"),


    DATA_ANALYSIS("데이터분석"),
    AI("인공지능"),
    DATA_VISUALIZATION("데이터 시각화"),
    DATA_COLLECTION_PROCESSING("데이터 수집 · 처리"),
    GAME_PROGRAMMING("게임 프로그래밍"),
    GAME_PLANNING("게임 기획"),
    GAME_ART_GRAPHICS("게임 아트 · 그래픽"),
    CHUNGKANG_UNIVERSITY_GAME_SCHOOL("청강대 게임스쿨"),


    COMPUTER_STRUCTURE("컴퓨터 구조"),
    EMBEDDED_IOT("임베디드 · IoT"),
    SEMICONDUCTOR("반도체"),
    ROBOTICS("로봇 공학"),
    MOBILITY("모빌리티"),


    CAD_3D_MODELING("CAD · 3D 모델링"),
    WEB_PUBLISHING("웹 퍼블리싱"),
    UX_UI("UX/UI"),
    GRAPHIC_DESIGN("그래픽 디자인"),
    DESIGN_TOOLS("디자인 툴"),
    PHOTO_FOOTAGE("사진 · 영상"),
    VR_AR("VR/AR"),
    SOUND("사운드"),


    OFFICE("오피스"),
    MARKETING("마케팅"),
    PLANNING_STRATEGY_PM("기획 · 전략 · PM"),
    AUTOMATION("업무 자동화"),
    MANAGEMENT("경영"),


    MATH("수학"),
    FOREIGN_LANGUAGE("외국어"),


    JOB_CHANGE("취업 · 이직"),
    PERSONAL_BRANDING("개인 브랜딩"),
    FOUNDED("창업"),


    FINANCIAL_INVESTMENT("금융 · 재태크"),
    CULTURE("교양"),


    CERTIFICATE("자격증"),
    ETC("기타");

    private String korName;

    SubCategory(String korName) {
        this.korName = korName;
    }

    public String getKorName() {
        return korName;
    }
}
