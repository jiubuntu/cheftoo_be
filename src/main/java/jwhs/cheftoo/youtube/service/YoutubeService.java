package jwhs.cheftoo.youtube.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jwhs.cheftoo.youtube.dto.YoutubeResponseDto;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class YoutubeService {

    private final RestTemplate restTemplate;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final String YOUTUBE_CACHE_KEY = "youtube:home:videos";

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
                String json = objectMapper.writeValueAsString(itemList);
                redisTemplate.opsForValue().set(YOUTUBE_CACHE_KEY, json, Duration.ofDays(7));
            } catch (JsonProcessingException e) {
                log.error("youtube videoId Redis 저장 중 에러가 발생하였습니다.", e.getMessage());
                throw new RuntimeException("Redis 저장 중 JSON 변환 실패", e);
            }
        }
    }

    // API로 노출
    public YoutubeResponseDto getCachedVideos() {

        String json = (String) redisTemplate.opsForValue().get(YOUTUBE_CACHE_KEY);
        if (json == null) return null;

        try {
            List<String> videoidList =  objectMapper.readValue(json, new TypeReference<List<String>>() {});
            return YoutubeResponseDto.builder()
                    .videoIdList(videoidList)
                    .build();
        } catch (JsonProcessingException e) {
            log.error("yotube Redis 조회 중 JSON 역직렬화 오류");
            throw new RuntimeException("Youtube Redis 조회 중 JSON 역직렬화 실패", e);
        }
    }



}
