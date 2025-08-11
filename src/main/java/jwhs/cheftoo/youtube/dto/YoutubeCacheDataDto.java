package jwhs.cheftoo.youtube.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class YoutubeCacheDataDto {
    private List<String> videoList;
    private String version;
}
