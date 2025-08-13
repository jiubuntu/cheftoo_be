package jwhs.cheftoo.auth.service;

import jakarta.transaction.Transactional;
import jwhs.cheftoo.auth.dto.Kakao;
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
    public Map<String, Object> loginWithKakao(String code) {
        String accessToken = kakaoUtil.getACcessToken(code);

        Kakao.KakaoUserInfo kakaoUserInfo = kakaoUtil.getUserInfo(accessToken);
        String kakaoUserId = String.valueOf(kakaoUserInfo.getId());

        // 카카오 회원 ID로 조회된 회원이 없으면 회원가입, 있으면 memberId 반환
//        Member member = memberRepository.findByKakaoId(kakaoUserId)
//                .orElseGet(() -> registerNewUser(kakaoUserId));
        Optional<Member> optionalMember = memberRepository.findByKakaoId(kakaoUserId);

        boolean isNewUser = optionalMember.isEmpty(); // 처음 가입한 사용자인지

        Member member = optionalMember.orElseGet(() -> registerNewUserWithKakaoId(kakaoUserId));
        UUID memberId = member.getMemberId();

        // redis에 카카오 액세스토큰 저장 (카카오 로그아웃 하기 위함)
        redisUtil.set(KakaoInfo.KAKAO_ACCESS_TOKEN_KEY.getAccessTokenKey() + memberId, accessToken);


        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("memberId", member.getMemberId());
        resultMap.put("isNewUser", isNewUser);
        resultMap.put("refreshToken", jwtUtil.generateRefreshToken(member.getMemberId()));
        resultMap.put("accessToken", jwtUtil.generateAccessToken(member.getMemberId()));

        return resultMap;
    }

    public Member getMemberByKakaoIngaCode(String code) {
        String accessToken = kakaoUtil.getACcessToken(code);
        Kakao.KakaoUserInfo kakaoUserInfo = kakaoUtil.getUserInfo(accessToken);
        String kakaoUserId = String.valueOf(kakaoUserInfo.getId());
        Optional<Member> optionalMember = memberRepository.findByKakaoId(kakaoUserId);
        Member member = optionalMember.orElse(null);
        return member;
    }

    public String getKakaoIdByKakaoIngaCode(String code) {
        String accessToken = kakaoUtil.getACcessToken(code);
        Kakao.KakaoUserInfo kakaoUserInfo = kakaoUtil.getUserInfo(accessToken);
        String kakaoUserId = String.valueOf(kakaoUserInfo.getId());
        return kakaoUserId;
    }


    // 회원가입처리
    @Transactional
    public Member registerNewUserWithKakaoId(String kakaoUserId) {
        Member newMember = new Member();
        newMember.setKakaoId(kakaoUserId);
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

