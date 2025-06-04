package jwhs.cheftoo.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jwhs.cheftoo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

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
            //JWT 토큰이 유효하지 않으면 다시 로그인 화면으로 이동
            response.sendRedirect(KAKAO_LOGIN_URL + "?client_id="+ KAKAO_CLIENT_ID + "&redirect_uri=" + REDIRECT_URL + "&response_type=code");
            return ;
        }

        // 토큰이 유효하면 Authentication 객체 생성 후 세팅
        UUID memberId = jwtUtil.getMemberIdFromToken(token);
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberId, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        filterChain.doFilter(request, response);

    }



}
