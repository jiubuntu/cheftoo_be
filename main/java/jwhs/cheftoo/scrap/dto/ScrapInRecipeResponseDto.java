package jwhs.cheftoo.scrap.dto;


import jwhs.cheftoo.recipe.dto.RecipeResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class ScrapInRecipeResponseDto {

    private UUID scrapId;
    List<RecipeResponseDto> recipeList;
}
