package online.lecture.entity.category;

import java.util.Arrays;
import java.util.List;

import static online.lecture.entity.category.SubCategory.*;

public enum Category {
    DEVELOPMENT_PROGRAMMING("개발 · 프로그래밍", WEB_DEVELOPMENT, FRONT_END, BACK_END, FULL_STACK,MOBILE_APP_DEVELOPMENT, GAME_DEVELOPMENT_SUB,PROGRAMMING_LANGUAGE,ALGORITHM_DATA_STRUCTURE,DATABASE,DEVOPS_INFRASTRUCTURE,CERTIFICATE,DEVELOPMENT_TOOL),
    SECURE_NETWORK("보안 · 네트워크",SECURITY,NETWORK,SYSTEM,CLOUD,BLOCKCHAIN,CERTIFICATE,ETC),
    DATA_SCIENCE("데이터 사이언스",DATA_ANALYSIS,AI,DATA_VISUALIZATION,DATA_COLLECTION_PROCESSING,CERTIFICATE,ETC),
    GAME_DEVELOPMENT_MAIN("게임 개발",GAME_PROGRAMMING,GAME_PLANNING,GAME_ART_GRAPHICS,ETC,CHUNGKANG_UNIVERSITY_GAME_SCHOOL),
    HARDWARE("하드웨어",COMPUTER_STRUCTURE,EMBEDDED_IOT,SEMICONDUCTOR,ROBOTICS,MOBILITY,CERTIFICATE,ETC),
    CREATIVE("크리에이티브",CAD_3D_MODELING,WEB_PUBLISHING,UX_UI,GRAPHIC_DESIGN,DESIGN_TOOLS,PHOTO_FOOTAGE,CERTIFICATE,VR_AR,SOUND,ETC),
    BUSINESS_MARKETING("비즈니스 · 마케팅",OFFICE,MARKETING,PLANNING_STRATEGY_PM,AUTOMATION,MANAGEMENT,CERTIFICATE,ETC),
    STUDY_FOREIGN_LANGUAGE("학문 · 외국어",MATH,FOREIGN_LANGUAGE,ETC),
    CAREER("커리어",JOB_CHANGE,PERSONAL_BRANDING,FOUNDED,ETC),
    SELF_DEVELOPMENT("자기계발",FINANCIAL_INVESTMENT,CULTURE);


    private String korName;
    private List<SubCategory> subCategories;
    Category(String korName,SubCategory... subCategories){
        this.korName = korName;
        this.subCategories = Arrays.asList(subCategories);
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

    public String getKorName() {
        return korName;
    }
}
