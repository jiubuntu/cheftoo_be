package jwhs.cheftoo.scrap.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ScrapInRecipeCheckAndCounDetailtDto {
    private boolean scrap;
    private long scrapCount ;
}
