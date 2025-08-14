package jwhs.cheftoo.auth.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jwhs.cheftoo.auth.entity.Member;
import jwhs.cheftoo.auth.service.KakaoService;
import jwhs.cheftoo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/oauth")
public class OauthController {

    private final KakaoService kakaoService;
    private final JwtUtil jwtUtil;

    // 카카오 로그인

    @GetMapping("/kakao/callback")
    public ResponseEntity<?> login(
            @RequestParam("code") String code,
            @RequestParam(value = "state") String state,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        Member member = kakaoService.getMemberByKakaoIngaCode(code);
        UUID memberId = member.getMemberId();

        if (member == null) {
            // 처음 가입한 사용자인경우 약관동의 세션 생성 및 리다이렉션
            String kakaoId = kakaoService.getKakaoIdByKakaoIngaCode(code);

            HttpSession session = request.getSession(false);
            if (session == null) {
                session = request.getSession(true);
                session.setMaxInactiveInterval(60 * 30); // 30분 뒤 타임아웃
                session.setAttribute("KAKAO_ID",kakaoId);
            }
            return ResponseEntity.ok("/terms");



        } else {
            // 리다이렉션 페이지 파싱
            String prevPage = "/";
            String nextPage = null;

            if (state != null) {
                try {
                    String decoded = URLDecoder.decode(state, StandardCharsets.UTF_8);
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String , String > parsed = objectMapper.readValue(decoded, new TypeReference<Map<String, String>>() {
                    });
                    prevPage = parsed.getOrDefault("prevPage", "/");
                    nextPage = parsed.get("nextPage");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // 리프레시 토큰 발급
            jwtUtil.addRefreshTokenToCookie(memberId, response, jwtUtil.generateRefreshToken(memberId));

            // 액세스 토큰 발급
            String accessToken = jwtUtil.generateAccessToken(memberId);
            // 리다이레션 페이지 설정
            String targetUrl = nextPage != null ? nextPage : prevPage ;

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("accessToken", accessToken);
            responseBody.put("redirectTo", targetUrl);
            return ResponseEntity.ok(responseBody);
        }
    }

}
