package jwhs.cheftoo.controller;

import jakarta.servlet.http.HttpServletResponse;
import jwhs.cheftoo.dto.Kakao;
import jwhs.cheftoo.service.KakaoService;
import jwhs.cheftoo.service.MemberService;
import jwhs.cheftoo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth/kakao")
public class KakaoController {

    private final KakaoService kakaoService;
    private final JwtUtil jwtUtil;

    @GetMapping("/callback")
    public ResponseEntity<?> loginKakao(@RequestParam("code") String code, HttpServletResponse response) {
        //jwt 발급
        String jwt = kakaoService.loginWithKakao(code);

        jwtUtil.addJwtToCookie(response, jwt);

        return ResponseEntity.ok().body(Map.of("message", "loginSuccessful"));
    }

}
