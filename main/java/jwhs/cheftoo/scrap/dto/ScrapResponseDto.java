package jwhs.cheftoo.scrap.dto;

import jwhs.cheftoo.scrap.entity.Scrap;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Builder
public class ScrapResponseDto {
    private UUID scrapId;
    private String scrapName;

    public static ScrapResponseDto fromEntity(Scrap scrap) {
        return ScrapResponseDto.builder()
                .scrapId(scrap.getScrapId())
                .scrapName(scrap.getScrapName())
                .build();
    }
}
