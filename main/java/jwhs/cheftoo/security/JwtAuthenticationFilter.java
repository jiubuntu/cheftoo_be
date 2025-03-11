package jwhs.cheftoo.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jwhs.cheftoo.util.JwtUtil;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// JWT 유효성 검증 필터
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if (requestURI.equals("/oauth/kakao/callback")) {
            filterChain.doFilter(request, response);
            return ;
        }

        String token = jwtUtil.getTokenFromRequest(request);

        if (!jwtUtil.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\" : \"Unauthorized\"}");
            return ;
        }

        filterChain.doFilter(request, response);

    }



}
