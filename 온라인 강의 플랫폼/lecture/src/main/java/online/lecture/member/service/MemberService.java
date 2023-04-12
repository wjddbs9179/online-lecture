package online.lecture.member.service;

import lombok.RequiredArgsConstructor;
import online.lecture.member.controller.domain.UpdateMemberForm;
import online.lecture.member.entity.Member;
import online.lecture.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Long join(Member member){
        Long savedId = memberRepository.save(member);
        return savedId;
    }

    public boolean update(Long id, UpdateMemberForm form ){
        return memberRepository.update(id,form);
    }

    public boolean delete(Long id){
        Member findMember = memberRepository.find(id);
        return memberRepository.delete(findMember);
    }

    @Transactional(readOnly = true)
    public Member info(Long id){
        return memberRepository.find(id);
    }

    public Member login(String userId, String password){
        return memberRepository.login(userId, password);
    }
}
