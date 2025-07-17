package jwhs.cheftoo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jwhs.cheftoo.auth.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.UUID;



@Component
@RequiredArgsConstructor
public class JwtUtil {
//    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // 시크릿 키 생성
    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    private final RefreshTokenService refreshTokenService;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);

    }
    private final long refreshTokenExpirationTime = 1000L * 60 * 60 * 24 * 14; // 2주
    private final long accessTokenExpriationTime = 900000; // 15분



    public String generateRefreshToken(UUID memberId) {
        return Jwts.builder()
                .setSubject(memberId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAccessToken(UUID memberId) {
        return Jwts.builder()
                .setSubject(memberId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpriationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    //토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public String getAccessTokenFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // "Bearer " 다음부터 잘라냄
        }

        return null; // 없거나 형식이 틀렸으면 null 반환

    }


    public String getRefreshTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;

    }

    // 리프레시토큰을 HttpOnly 쿠키로 저장하여 클라이언트로 전송
    public void addRefreshTokenToCookie(UUID memberId, HttpServletResponse response, String token) throws IOException {
        try {
            // redis에 리프레시 토큰 저장
            refreshTokenService.saveRefreshToken(memberId, token, refreshTokenExpirationTime);
        } catch (IllegalStateException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "로그인 실패 : 서버 오류");
        }
        Cookie setCookie = setRefreshTokenInCookie(token, refreshTokenExpirationTime);

        response.addCookie(setCookie); // 클라이언트에 쿠키 저장
    }

    public Cookie setRefreshTokenInCookie(String token, long maxAgeMilSec) {
        Cookie cookie = new Cookie("refreshToken", token);
        cookie.setHttpOnly(true);
//        cookie.setSecure(true); // https에서만 전송
        cookie.setPath("/");
        cookie.setMaxAge((int) (maxAgeMilSec / 1000));
        return cookie;
    }

    // 액세스 토큰 발급
    public void sendAccessToken(HttpServletResponse response, String accessToken) throws IOException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String json = new ObjectMapper().writeValueAsString(
                Map.of("accessToken", accessToken)
        );

        response.getWriter().write(json);
    }



    // JWT로부터 MemberId 추출
    public UUID getMemberIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return UUID.fromString(claims.getSubject());
    }

    public void deleteRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
//        cookie.setSecure(true);
        cookie.setMaxAge(0);    // 즉시 만료
        response.addCookie(cookie);
    }
}
