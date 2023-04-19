package online.lecture.member.service;

import lombok.RequiredArgsConstructor;
import online.lecture.entity.Lecture;
import online.lecture.entity.MemberLecture;
import online.lecture.lecture.repository.LectureRepository;
import online.lecture.member.controller.domain.IdSearchForm;
import online.lecture.member.controller.domain.PwSearchForm;
import online.lecture.member.controller.domain.UpdateMemberForm;
import online.lecture.entity.Member;
import online.lecture.member.repository.MemberLectureRepository;
import online.lecture.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final LectureRepository lectureRepository;
    private final MemberLectureRepository memberLectureRepository;

    public Long join(Member member) {
        Long savedId = memberRepository.save(member);
        return savedId;
    }

    public boolean update(Long id, UpdateMemberForm form) {
        return memberRepository.update(id, form);
    }

    public boolean delete(Long id) {
        Member findMember = memberRepository.find(id);
        return memberRepository.delete(findMember);
    }

    @Transactional(readOnly = true)
    public Member info(Long id) {
        return memberRepository.find(id);
    }

    public Member login(String userId, String password) {
        return memberRepository.login(userId, password);
    }

    public Member idSearch(IdSearchForm form) {
        return memberRepository.idSearch(form);
    }

    public Member pwSearch(PwSearchForm form) {
        Member findMember = memberRepository.pwSearch(form);
        UpdateMemberForm updateMemberForm = new UpdateMemberForm();

        if (findMember != null) {
            String[] randomChar = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
                    , "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"
                    , "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

            String newPassword = "";

            for (int i = 0; i < 13; i++) {
                newPassword += randomChar[(int) (Math.random() * randomChar.length)];
            }

            updateMemberForm.setPassword(newPassword);
            updateMemberForm.setUsername(findMember.getUsername());
            updateMemberForm.setEmail(findMember.getEmail());


            findMember.update(updateMemberForm);
        }
        return findMember;
    }

    public void enrolment(Long memberId, Long lectureId) {
        Member member = info(memberId);
        Lecture lecture = lectureRepository.find(lectureId);

        MemberLecture memberLecture = new MemberLecture(member,lecture);
        member.addLecture(memberLecture);
    }

    public List<Lecture> myLecture(Long memberId) {

        Member member = memberRepository.find(memberId);
        return memberLectureRepository.myLecture(member);
    }

    public boolean userIdUniqueCheck(String userId) {
        return memberRepository.findByUserId(userId);
    }
}
