package jwhs.cheftoo.youtube.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jwhs.cheftoo.util.port.RedisUtil;
import jwhs.cheftoo.youtube.dto.YoutubeCacheDataDto;
import jwhs.cheftoo.youtube.dto.YoutubeResponseDto;
import jwhs.cheftoo.youtube.enums.Youtube;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class YoutubeService {

    private final RestTemplate restTemplate;
    private final RedisUtil redisUtil;
    private final ObjectMapper objectMapper;


    @Value("${spring.datasource.youtube.api.key}")
    private String apiKey;

    @Value("${spring.datasource.youtube.api.url}")
    private String apiUrl;

    // 스케줄러가 호출
    public void fetchAndCacheYouTubeVideos() {
        String reqUrl = apiUrl + apiKey;

        ResponseEntity<Map> response = restTemplate.getForEntity(reqUrl, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            List<Map<String, Object>> items = (List<Map<String, Object>>) response.getBody().get("items");

            List<String> itemList = items.stream()
                    .map((item) -> {
                        Map<String, Object> id = (Map<String, Object>) item.get("id");
                        return (String) id.get("videoId");
                    })
                    .toList();

            try {
//                redisUtil.set(Youtube.YOUTUBE_CACHE_KEY.getCacheKey(), itemList);
                redisUtil.rightPushForList(Youtube.YOUTUBE_CACHE_KEY.getCacheKey(), itemList);

                String version = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                redisUtil.set(Youtube.YOUTUBE_VERSION_KEY.getCacheKey(), version);

            } catch (Exception e) {
                log.error("youtube videoId Redis 저장 중 에러가 발생하였습니다.", e.getMessage());
                throw new RuntimeException("Redis 저장 중 오류 발생", e);
            }
        }
    }


    public List<String> getCachedVideos() {
        String key = Youtube.YOUTUBE_CACHE_KEY.getCacheKey();
        Long size = redisUtil.getSizeForList(key);
        if (size == null || size == 0) return Collections.emptyList();
        List<Object> raw = redisUtil.range(key, 0, -1);
        return raw.stream().map(Object::toString).toList();
    }


    public String getYoutubeVideoVersion() {

        String version = null;

        try {
            version = redisUtil.get(Youtube.YOUTUBE_VERSION_KEY.getCacheKey());
            if (version == null) return null;
        } catch (Exception e) {
            log.error("유튜브 버전 redis 조회 실패");
        }

        return version;


    }



}
