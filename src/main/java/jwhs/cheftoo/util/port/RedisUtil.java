package jwhs.cheftoo.util.port;

import java.time.Duration;

public interface RedisUtil {

    public String get(String key);
    public void set(String key, Object Value);
}
