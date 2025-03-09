package jwhs.cheftoo.service;

import jwhs.cheftoo.entity.Member;
import jwhs.cheftoo.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    private MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Optional<Member> getBykakaoId(String kakaoId ) {
        return memberRepository.findByKakaoId(kakaoId);
    }



}
