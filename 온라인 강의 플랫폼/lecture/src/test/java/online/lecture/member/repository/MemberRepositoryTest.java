package online.lecture.member.repository;

import lombok.extern.slf4j.Slf4j;
import online.lecture.member.controller.domain.UpdateMemberForm;
import online.lecture.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
@Slf4j
@Transactional
class MemberRepositoryTest {

    @Autowired
    private MemberRepository repository;

    @Test
    void save(){
        Member member = new Member("user1","user1","1234","user@naver.com");
        Long savedId = repository.save(member);

        log.info("savedId = {}",savedId);
    }
    @Test
    void find(){
        Member member = new Member("user1","user1","1234","user@naver.com");
        repository.save(member);
        Member findMember = repository.find(member.getId());
        log.info("findMember = {}",findMember);
    }
    @Test
    void update(){
        Member member = new Member("user1","user1","1234","user@naver.com");
        repository.save(member);
        UpdateMemberForm form = new UpdateMemberForm();
        form.setUsername("updateUser");
        form.setEmail("updateUser@naver.com");
        form.setUsername("updateUsername");
        boolean update = repository.update(member.getId(), form);

        log.info("member = {}",member);
        log.info("update Result = {}",update);
    }
    @Test
    void delete(){
        Member member = new Member("user1","user1","1234","user@naver.com");
        repository.save(member);

        boolean result = repository.delete(member);
        log.info("result = {}",result);

    }
}