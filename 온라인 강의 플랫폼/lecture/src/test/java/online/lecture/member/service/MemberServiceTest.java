package online.lecture.member.service;

import lombok.extern.slf4j.Slf4j;
import online.lecture.member.controller.domain.UpdateMemberForm;
import online.lecture.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService service;

    @Test
    void test(){
        log.info("service = {}",service);
    }


    @Test
    void join(){
        Long result = service.join(new Member("user1", "username1", "password1", "email@email.come"));
        log.info("join result = {}",result);

        assertThat(result).isNotNull();
    }

    @Test
    void update(){
        Long id = service.join(new Member("user1", "username1", "password1", "email@email.come"));
        UpdateMemberForm form = new UpdateMemberForm();
        form.setUsername("updateName");
        form.setEmail("updateEmail@naver.com");
        form.setPassword("updatePassword");

        boolean result = service.update(id, form);
        log.info("result = {}", result);

        Member findMember = service.info(id);

        assertThat(findMember.getUsername()).isEqualTo("updateName");
        assertThat(findMember.getPassword()).isEqualTo("updatePassword");
        assertThat(findMember.getEmail()).isEqualTo("updateEmail@naver.com");
    }

    @Test
    void delete(){
        Long id = service.join(new Member("user1", "username1", "password1", "email@email.come"));
        boolean result = service.delete(id);

        log.info("result = {}",result);

        Member member = service.info(id);

        log.info("member = {}", member);

        assertThat(member).isNull();
    }

    @Test
    void info(){
        Long id = service.join(new Member("user1", "username1", "password1", "email@email.come"));

        Member findMember = service.info(id);

        assertThat(findMember.getId()).isEqualTo(id);
        assertThat(findMember.getUserId()).isEqualTo("user1");
        assertThat(findMember.getUsername()).isEqualTo("username1");
        assertThat(findMember.getPassword()).isEqualTo("password1");
        assertThat(findMember.getEmail()).isEqualTo("email@email.come");
    }

    @Test
    void login(){
        Long id = service.join(new Member("user1", "username1", "password1", "email@email.come"));

        Member loginMember = service.login("user1", "password1");

        log.info("loginMember = {}",loginMember);

        assertThat(loginMember.getEmail()).isEqualTo("email@email.come");
        assertThat(loginMember.getUserId()).isEqualTo("user1");
        assertThat(loginMember.getPassword()).isEqualTo("password1");
        assertThat(loginMember.getUsername()).isEqualTo("username1");
    }
}