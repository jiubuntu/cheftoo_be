package jwhs.cheftoo.controller;

import jwhs.cheftoo.dto.Kakao;
import jwhs.cheftoo.service.KakaoService;
import jwhs.cheftoo.service.MemberService;
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

    @GetMapping("/callback")
    //인가코드 받는 엔드포인트
    public ResponseEntity<?> loginKakao(@RequestParam("code") String code) {
        //jwt 발급
        String jwt = kakaoService.loginWithKakao(code);
        return ResponseEntity.ok().body(Map.of("token", jwt));
    }

}
