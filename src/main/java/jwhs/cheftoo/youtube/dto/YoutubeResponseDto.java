package jwhs.cheftoo.youtube.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class YoutubeResponseDto {

    private List<String> videoIdList;
}
