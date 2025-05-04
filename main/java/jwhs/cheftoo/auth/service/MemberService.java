package jwhs.cheftoo.auth.service;


import jakarta.transaction.Transactional;
import jwhs.cheftoo.auth.entity.Member;
import jwhs.cheftoo.auth.exception.MemberNotFoundException;
import jwhs.cheftoo.auth.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MemberService {

    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void updateNickname(UUID memberId, String nickname) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("해당 유저가 존재하지 않습니다."));
        member.setNickname(nickname);
    }

    public Member findMemberById(UUID memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> {
            throw new MemberNotFoundException("유저를 찾을 수 없습니다.");
        });
    }

}
