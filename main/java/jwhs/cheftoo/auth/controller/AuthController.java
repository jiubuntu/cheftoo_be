package jwhs.cheftoo.auth.controller;


import jakarta.servlet.http.HttpServletRequest;
import jwhs.cheftoo.auth.service.MemberService;
import jwhs.cheftoo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final JwtUtil jwtUtil;
    private final MemberService memberService;


    // 현재 브라우저가 가진 jwt가 유효한지 체크 (홈화면 - 레시피 등록에서 사용)
    @GetMapping("/check")
    public ResponseEntity<?> checkAuthSatatus(@CookieValue(value="jwt", required = false) String jwt) {
        if (jwt == null || !jwtUtil.validateToken(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","유효하지 않은 토큰"));

        }
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message","유효한 토큰"));
    }

    // 닉네임 설정
    @PutMapping("/nickname")
    public ResponseEntity<?> setNickName(
            @RequestParam String nickname,
            HttpServletRequest request
    ) {
       String token = jwtUtil.getTokenFromRequest(request);
       UUID memberId = jwtUtil.getMemberIdFromToken(token);

       memberService.updateNickname(memberId, nickname);

       return ResponseEntity.noContent().build(); // HttpsStatusCode = 204

    }
}
