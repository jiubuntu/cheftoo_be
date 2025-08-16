package jwhs.cheftoo.auth.service;

import jakarta.transaction.Transactional;
import jwhs.cheftoo.auth.dto.Kakao;
import jwhs.cheftoo.auth.dto.MemberConsentRequestDto;
import jwhs.cheftoo.auth.entity.Member;
import jwhs.cheftoo.auth.enums.KakaoInfo;
import jwhs.cheftoo.auth.port.MemberReader;
import jwhs.cheftoo.auth.repository.MemberRepository;
import jwhs.cheftoo.util.JwtUtil;
import jwhs.cheftoo.util.KakaoUtil;
import jwhs.cheftoo.util.port.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoService {

    private final MemberRepository memberRepository;
    private final KakaoUtil kakaoUtil;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final MemberReader memberReader;

    // 카카오 로그인 처리

    public Member getMemberByKakaoIngaCode(String code) {
        String accessToken = kakaoUtil.getAccessToken(code);
        Kakao.KakaoUserInfo kakaoUserInfo = kakaoUtil.getUserInfo(accessToken);
        String kakaoUserId = String.valueOf(kakaoUserInfo.getId());
        Optional<Member> optionalMember = memberRepository.findByKakaoId(kakaoUserId);
        Member member = optionalMember.orElse(null);
        return member;
    }

    public Map<String, String> getKakaoIdAndAccessTokenByKakaoIngaCode(String code) {
        String accessToken = kakaoUtil.getAccessToken(code);
        Kakao.KakaoUserInfo kakaoUserInfo = kakaoUtil.getUserInfo(accessToken);
        String kakaoUserId = String.valueOf(kakaoUserInfo.getId());

        Map<String, String> result = new HashMap<>();
        result.put("kakaoAccessToken", accessToken);
        result.put("kakaoUserId", kakaoUserId);

        return result;
    }

    public String getKakaoAccessTokenFromIngaCode(String code) {
        return kakaoUtil.getAccessToken(code);
    }


    // 회원가입처리
    @Transactional
    public Member registerNewUserWithKakao(MemberConsentRequestDto dto, String kakaoUserId) {
        String nickName = dto.getNickName();

        Member newMember = new Member();
        newMember.setKakaoId(kakaoUserId);
        newMember.setNickname(nickName);
        return memberRepository.save(newMember);
    }

    public void unlinkWithKakao(UUID memberId) {
        String accessToken = redisUtil.get(KakaoInfo.KAKAO_ACCESS_TOKEN_KEY.getAccessTokenKey() + memberId);

        kakaoUtil.unlinkWithAccessToken(accessToken);
    }

    public void logoutWithKakao(UUID memberId) {
        String accessToken = redisUtil.get(KakaoInfo.KAKAO_ACCESS_TOKEN_KEY.getAccessTokenKey() + memberId);

        kakaoUtil.logoutWithAccessToken(accessToken);
    }
}

