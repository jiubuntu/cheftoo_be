package jwhs.cheftoo.controller;

import jakarta.servlet.http.HttpServletResponse;
import jwhs.cheftoo.service.KakaoService;
import jwhs.cheftoo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth")
public class OauthController {

    private final KakaoService kakaoService;
    private final JwtUtil jwtUtil;

    // 카카오 로그인
    @GetMapping("/kakao/callback")
    public ResponseEntity<?> loginKakao(@RequestParam("code") String code, HttpServletResponse response) {
        //jwt 발급 및 닉네임 설정을 위한 첫 로그인 여부 파악
        // 처음 로그인하는 사용자라면 -> 닉네임 설정 , 기존 유저라면 -> 그냥 로그인
        Map<String, Object> map = kakaoService.loginWithKakao(code);

        jwtUtil.addJwtToCookie(response, (String) map.get("jwt")); // HttpOnly 방식으로 쿠키에 jwt담아 리턴

        return ResponseEntity.ok().body(Map.of(
                "message", "loginSuccessful",
                "isNewUser", map.get("isNewUser")
        ));
    }

}
