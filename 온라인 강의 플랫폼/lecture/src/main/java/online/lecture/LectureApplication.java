package online.lecture;

import lombok.RequiredArgsConstructor;
import online.lecture.admin.controller.domain.AdminUpdateForm;
import online.lecture.admin.repository.AdminRepository;
import online.lecture.entity.member.Admin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;

import javax.annotation.PostConstruct;

@SpringBootApplication
@RequiredArgsConstructor
public class LectureApplication {

	private final AdminRepository adminRepository;

	public static void main(String[] args) {
		SpringApplication.run(LectureApplication.class, args);
	}


}
