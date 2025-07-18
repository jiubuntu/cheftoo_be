package jwhs.cheftoo.auth.service;

import jwhs.cheftoo.auth.dto.Kakao;
import jwhs.cheftoo.auth.entity.Member;
import jwhs.cheftoo.auth.repository.MemberRepository;
import jwhs.cheftoo.util.JwtUtil;
import jwhs.cheftoo.util.KakaoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoService {

    private final MemberRepository memberRepository;
    private final KakaoUtil kakaoUtil;
    private final JwtUtil jwtUtil;

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

        Member member = optionalMember.orElseGet(() -> registerNewUser(kakaoUserId));

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("memberId", member.getMemberId());
        resultMap.put("isNewUser", isNewUser);
        resultMap.put("refreshToken", jwtUtil.generateRefreshToken(member.getMemberId()));
        resultMap.put("accessToken", jwtUtil.generateAccessToken(member.getMemberId()));

        return resultMap;
    }


    // 회원가입처리
    private Member registerNewUser(String kakaoUserId) {
        Member newMember = new Member();
        newMember.setKakaoId(kakaoUserId);
        return memberRepository.save(newMember);
    }
}

