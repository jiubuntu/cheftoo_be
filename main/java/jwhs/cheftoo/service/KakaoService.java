package jwhs.cheftoo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jwhs.cheftoo.dto.Kakao;
import jwhs.cheftoo.entity.Member;
import jwhs.cheftoo.repository.MemberRepository;
import jwhs.cheftoo.util.JwtUtil;
import jwhs.cheftoo.util.KakaoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

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
        resultMap.put("isNewUser", isNewUser);
        resultMap.put("jwt", jwtUtil.generateToken(String.valueOf(member.getMemberId())));

        return resultMap;
    }


    // 회원가입처리
    private Member registerNewUser(String kakaoUserId) {
        Member newMember = new Member();
        newMember.setKakaoId(kakaoUserId);
        return memberRepository.save(newMember);
    }
}

