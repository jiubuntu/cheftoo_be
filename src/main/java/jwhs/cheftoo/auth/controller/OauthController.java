package jwhs.cheftoo.auth.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import jwhs.cheftoo.auth.service.KakaoService;
import jwhs.cheftoo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<?> loginKakao(
            @RequestParam("code") String code,
            @RequestParam(value = "state") String state,
            HttpServletResponse response
    ) throws IOException {

        // JSON 파싱
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

        //jwt 발급 및 닉네임 설정을 위한 첫 로그인 여부 파악
        // 처음 로그인하는 사용자라면 -> 닉네임 설정 , 기존 유저라면 -> 그냥 로그인
        Map<String, Object> map = kakaoService.loginWithKakao(code);

        String refreshToken = (String) map.get("refreshToken");
        String accessToken = (String) map.get("accessToken");
        boolean isNewUser = (boolean) map.get("isNewUser");

        //리프레시 토큰 발급 (Http Only)
        jwtUtil.addRefreshTokenToCookie((UUID) map.get("memberId"), response, refreshToken); // HttpOnly 방식으로 쿠키에 jwt담아 리턴

        // 다음페이지값이 있으면 다음 페이지로 이동
        // 다음페이지 값이 있는 경우 : 레시피 화면(prevPage) -> 레시피 등록 화면(nextPage)
        String targetUrl = isNewUser ? "nickname" : (nextPage != null ? nextPage : prevPage);
//        response.sendRedirect("http://localhost:3000" + targetUrl + "?isNewUser=" + isNewUser);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("accessToken", accessToken);
        responseBody.put("isNewUser", isNewUser);
        responseBody.put("redirectTo", targetUrl);

        return ResponseEntity.ok(responseBody);

    }

}
