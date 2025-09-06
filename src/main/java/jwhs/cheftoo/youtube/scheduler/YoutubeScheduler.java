package jwhs.cheftoo.youtube.scheduler;

import jwhs.cheftoo.youtube.service.YoutubeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class YoutubeScheduler {
    private final YoutubeService youTubeService;

    // 매주 월요일 오전 3시
//    @Scheduled(cron = "0 0 3 ? * MON")
    @Scheduled(fixedDelay = 10000) // 테스트용
    public void updateYouTubeCacheWeekly() {
        log.info("Youtube VideoId 갱신 스케쥴러 시작");
        youTubeService.fetchAndCacheYouTubeVideos();
    }
}
