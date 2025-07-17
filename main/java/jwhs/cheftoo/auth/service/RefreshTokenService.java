package jwhs.cheftoo.auth.service;


import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final StringRedisTemplate redisTemplate;

    // 저장 (토큰 만료시간 = 2주)
    public void saveRefreshToken(UUID memberId, String refreshToken, long expirationMillis) {
        try {
            ValueOperations<String, String> ops = redisTemplate.opsForValue();
            ops.set("refreshToken:" + memberId, refreshToken, expirationMillis, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("[Redis 저장 실패] memberId: {} 리프레시 토큰 저장 중 에러", memberId, e);
            throw new IllegalStateException("리프레시 토큰 저장 실패");
        }

    }

    // 조회
    public String getRefreshToken(UUID memberId) {
        try {
            return redisTemplate.opsForValue().get("refreshToken:" + memberId);
        } catch (Exception e) {
            log.error("[Redis 조회 실패] memberId: {}", memberId, e);
            throw new IllegalStateException("서버 오류: 토큰 조회 실패");
        }
    }


    public void deleteRefreshToken(UUID memberId) {
        try {
            redisTemplate.delete("refreshToken:" + memberId);
        } catch (Exception e) {
            log.error("[Redis 삭제 실패] memberId: {}", memberId, e);
            throw new RedisException("Redis 데이터 삭제 중 에러 발생");
        }
    }

}
