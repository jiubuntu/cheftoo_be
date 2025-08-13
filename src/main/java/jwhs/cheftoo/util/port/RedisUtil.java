package jwhs.cheftoo.util.port;

import java.time.Duration;
import java.util.List;

public interface RedisUtil {

    public String get(String key);
    public void set(String key, Object Value);

    public void rightPushForList(String key , List<?> list);

    public long getSizeForList(String key);

    public List<Object> range(String key, int startIdx, int endIdx);
}
