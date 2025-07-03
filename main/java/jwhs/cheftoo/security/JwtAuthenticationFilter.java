package jwhs.cheftoo.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jwhs.cheftoo.auth.service.RefreshTokenService;
import jwhs.cheftoo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

// JWT 유효성 검증 필터
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${kakao.login.url}")
    private String KAKAO_LOGIN_URL;
    @Value("${kakao.client.id}")
    private String KAKAO_CLIENT_ID;
    @Value("${kakao.redirect.url}")
    private String REDIRECT_URL;
    private final JwtUtil jwtUtil;
    @Autowired
    private RefreshTokenService refreshTokenService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if (requestURI.equals("/oauth/kakao/callback")) { // 로그인 및 회원가입 요청은 JWT 검증 X
            filterChain.doFilter(request, response);
            return ;
        }

        String accessToken = jwtUtil.getAccessTokenFromRequest(request);


        if(accessToken == null) {
            filterChain.doFilter(request, response);
            return ;
        }

        // 액세스 토큰이 유효하면 인증처리
        if (accessToken != null && jwtUtil.validateToken(accessToken)) {
            UUID memberId = jwtUtil.getMemberIdFromToken(accessToken);
            setAuthentication(memberId);
            filterChain.doFilter(request, response);
            return ;
        }

        // 액세스 토큰이 휴효하지 않으면 401 처리
        if (accessToken != null && !jwtUtil.validateToken(accessToken)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }

    // 인증 처리
    public void setAuthentication(UUID memberId) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberId, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }



//    public void handleTokenReissue(String refreshToken, HttpServletResponse response) throws IOException {
//        UUID memberId = jwtUtil.getMemberIdFromToken(refreshToken);
//        String savedRefreshToken = null;
//        try {
//            savedRefreshToken = refreshTokenService.getRefreshToken(memberId);
//        } catch (Exception e) {
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "로그인 실패 : 서버 오류");
//            return ;
//        }
//
//        if (Objects.equals(refreshToken, savedRefreshToken)) {
//            String newAccessToken = jwtUtil.generateAccessToken(memberId);
//            jwtUtil.sendAccessToken(response, newAccessToken);
//        } else { // 리프레시 토큰 삭제 후, 재로그인
//            try {
//                refreshTokenService.deleteRefreshToken(memberId);
//            } catch (Exception e) {
//                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "로그인 실패 : 서버 오류");
//                return ;
//            }
//
//            redirectToLogin(response);
//        }
//
//    }
//
//    public void redirectToLogin(HttpServletResponse response) throws IOException {
//        String kakaoLoginUrl = KAKAO_LOGIN_URL + "?client_id="+ KAKAO_CLIENT_ID + "&redirect_uri=" + REDIRECT_URL + "&response_type=code";
//        response.sendRedirect(kakaoLoginUrl);
//    }

}
