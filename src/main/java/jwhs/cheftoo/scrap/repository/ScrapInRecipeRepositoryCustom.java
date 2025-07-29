package jwhs.cheftoo.scrap.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import jwhs.cheftoo.recipe.dto.RecipeResponseDto;
import jwhs.cheftoo.recipe.entity.Recipe;
import jwhs.cheftoo.scrap.dto.ScrapInRecipeCheckAndCounDetailtDto;

import java.util.List;
import java.util.UUID;

public interface ScrapInRecipeRepositoryCustom {
    List<RecipeResponseDto> findRecipesInScrap(UUID scrapID);
    ScrapInRecipeCheckAndCounDetailtDto checkScrapAndCount(UUID recipeId, UUID memberId);
}
