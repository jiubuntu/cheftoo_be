package jwhs.cheftoo.scrap.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Builder
public class ScrapResponseDto {
    private UUID scrapId;
    private String scrapName;
}
