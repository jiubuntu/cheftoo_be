package jwhs.cheftoo.youtube.controller;

import jwhs.cheftoo.youtube.dto.YoutubeResponseDto;
import jwhs.cheftoo.youtube.service.YoutubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/youtube")
@RequiredArgsConstructor
public class YoutubeController {

    private final YoutubeService youtubeService;


    @GetMapping("/home-videos")
    public ResponseEntity<YoutubeResponseDto> getHomeVideos() {
        return ResponseEntity.ok(youtubeService.getCachedVideos());
    }
}
