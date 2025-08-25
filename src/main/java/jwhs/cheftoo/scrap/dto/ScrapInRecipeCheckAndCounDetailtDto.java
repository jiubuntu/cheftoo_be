package jwhs.cheftoo.scrap.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class ScrapInRecipeCheckAndCounDetailtDto {
    private boolean scrap;
    private UUID scrapId;
    private long scrapCount ;
}
