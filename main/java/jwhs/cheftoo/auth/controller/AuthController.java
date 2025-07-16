package jwhs.cheftoo.auth.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jwhs.cheftoo.auth.service.MemberService;
import jwhs.cheftoo.auth.service.RefreshTokenService;
import jwhs.cheftoo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final JwtUtil jwtUtil;
    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;

    @Value("${kakao.login.url}")
    private String KAKAO_LOGIN_URL;
    @Value("${kakao.client.id}")
    private String KAKAO_CLIENT_ID;
    @Value("${kakao.redirect.url}")
    private String REDIRECT_URL;


    // 현재 브라우저가 가진 jwt가 유효한지 체크 (홈화면 - 레시피 등록에서 사용)
    @GetMapping("/check")
    public ResponseEntity<?> checkAuthSatatus(
            HttpServletRequest request
    ) {
        String accessToken = jwtUtil.getAccessTokenFromRequest(request);
        if (accessToken == null || !jwtUtil.validateToken(accessToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","유효하지 않은 토큰"));

        }
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message","유효한 토큰"));
    }

    // 닉네임 설정
    @PutMapping("/nickname")
    public ResponseEntity<?> setNickName(
            @RequestParam("nickname") String nickname,
            HttpServletRequest request
    ) {
       String token = jwtUtil.getAccessTokenFromRequest(request);
       UUID memberId = jwtUtil.getMemberIdFromToken(token);

       memberService.updateNickname(memberId, nickname);

       return ResponseEntity.noContent().build(); // HttpsStatusCode = 204

    }

    // 멤버ID를 통해 닉네임 조회
    @GetMapping("/nickname")
    public ResponseEntity<?> getNickNameByMember(HttpServletRequest request) {
        String token = jwtUtil.getAccessTokenFromRequest(request);
        UUID memberId = jwtUtil.getMemberIdFromToken(token);

        return ResponseEntity.status(HttpStatus.OK).body(memberService.findNickNameByMemberId(memberId));

    }

    @PostMapping("refresh")
    public void refresh(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        String refreshToken = jwtUtil.getRefreshTokenFromRequest(request);
        String token = jwtUtil.getAccessTokenFromRequest(request);
        UUID memberId = jwtUtil.getMemberIdFromToken(token);
        String savedRefreshToken = null;

        try {
            savedRefreshToken = refreshTokenService.getRefreshToken(memberId);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "로그인 실패 : 서버 오류");
            return;
        }

        if( Objects.equals(refreshToken, savedRefreshToken)) {
            // 기존 저장된 리프레시토큰과 일치한다면 액세스 토큰 발급
            String newAccessToken = jwtUtil.generateAccessToken(memberId);
            jwtUtil.sendAccessToken(response, newAccessToken);
        } else {
            // 기존 저장된 리프레시토큰과 일치하지 않으면 로그인 화면 이동
            try {
                refreshTokenService.deleteRefreshToken(memberId);
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "로그인 실패 : 서버 오류");
                return ;
            }

            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인 실패 : 리프레시 토큰 불일치");
//            String kakaoLoginUrl = KAKAO_LOGIN_URL + "?client_id="+ KAKAO_CLIENT_ID + "&redirect_uri=" + REDIRECT_URL + "&response_type=code";
//            response.sendRedirect(kakaoLoginUrl);
        }
    }

//    @PostMapping("logout")
//    public ResponseEntity<?> logout(
//            HttpServletRequest request
//    ) {
//        String token = jwtUtil.getAccessTokenFromRequest(request);
//        UUID memberId = jwtUtil.getMemberIdFromToken(token);
//
//        refreshTokenService.deleteRefreshToken(memberId);
//
//    }
}
