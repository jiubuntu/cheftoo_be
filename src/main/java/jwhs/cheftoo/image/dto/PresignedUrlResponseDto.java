package jwhs.cheftoo.image.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PresignedUrlResponseDto {
    private String url;
    private String key;
}
