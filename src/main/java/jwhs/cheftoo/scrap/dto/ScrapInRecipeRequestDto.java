package jwhs.cheftoo.scrap.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScrapInRecipeRequestDto {

    private UUID scrapId;

}
