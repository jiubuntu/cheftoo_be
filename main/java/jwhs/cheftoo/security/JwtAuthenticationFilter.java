package jwhs.cheftoo.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jwhs.cheftoo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// JWT 유효성 검증 필터
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${kakao.login.url}")
    String KAKAO_LOGIN_URL;
    @Value("${kakao.client.id}")
    private String KAKAO_CLIENT_ID;
    @Value("${kakao.redirect.url}")
    private String REDIRECT_URL;
    private final JwtUtil jwtUtil;

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

        String token = jwtUtil.getTokenFromRequest(request);

        if (!jwtUtil.validateToken(token)) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("{\"error\" : \"Unauthorized\"}");
            //JWT 토큰이 유효하지 않으면 다시 로그인 화면으로 이동
            response.sendRedirect(KAKAO_LOGIN_URL + "?client_id="+ KAKAO_CLIENT_ID + "&redirect_uri=" + REDIRECT_URL + "&response_type=code");
            return ;
        }

        filterChain.doFilter(request, response);

    }



}
