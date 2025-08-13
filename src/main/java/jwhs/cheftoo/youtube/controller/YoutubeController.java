package jwhs.cheftoo.youtube.controller;

import jwhs.cheftoo.util.port.RedisUtil;
import jwhs.cheftoo.youtube.dto.YoutubeResponseDto;
import jwhs.cheftoo.youtube.enums.Youtube;
import jwhs.cheftoo.youtube.service.YoutubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/youtube")
@RequiredArgsConstructor
public class YoutubeController {

    private final YoutubeService youtubeService;
    private final RedisUtil redisUtil;


    @GetMapping("/home-videos")
    public ResponseEntity<List<String>> getHomeVideos() {
        return ResponseEntity.ok().body(youtubeService.getCachedVideos());
    }

    @GetMapping("/version")
    public String getYoutubeVideoVersion() {
        return youtubeService.getYoutubeVideoVersion();
    }
}
