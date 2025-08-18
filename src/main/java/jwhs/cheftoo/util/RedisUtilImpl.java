package jwhs.cheftoo.util;

import jwhs.cheftoo.util.port.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@RequiredArgsConstructor
@Component
public class RedisUtilImpl implements RedisUtil {

    private final RedisTemplate<String ,Object> redisTemplate;

    @Override
    public String get(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        return value != null ? value.toString() : null;
    }

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void rightPushForList(String key, List<?> list) {
        redisTemplate.opsForList().rightPushAll(key, list);
    }

    @Override
    public long getSizeForList(String key) {
        return redisTemplate.opsForList().size(key);
    }

    @Override
    public List<Object> range(String key, int startIdx, int endIdx) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }


}
