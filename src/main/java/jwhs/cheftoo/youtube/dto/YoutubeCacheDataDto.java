package jwhs.cheftoo.youtube.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class YoutubeCacheDataDto {
    private List<String> videoIdList;
    private String version;
}
