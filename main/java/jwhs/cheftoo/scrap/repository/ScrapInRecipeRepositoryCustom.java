package jwhs.cheftoo.scrap.repository;

import jwhs.cheftoo.recipe.dto.RecipeResponseDto;
import jwhs.cheftoo.recipe.entity.Recipe;

import java.util.List;
import java.util.UUID;

public interface ScrapInRecipeRepositoryCustom {
    List<RecipeResponseDto> findRecipesInScrap(UUID scrapID);
}
