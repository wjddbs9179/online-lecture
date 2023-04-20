package online.lecture.Interceptor.config;

import online.lecture.Interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/lecture/filter/**","/member/login","/","/member/join","/member/id-search","/member/pw-search","/member/cNumCheck-pw"
                ,"/member/cNumCheck-Id","/lecture/images/**");
    }
}
