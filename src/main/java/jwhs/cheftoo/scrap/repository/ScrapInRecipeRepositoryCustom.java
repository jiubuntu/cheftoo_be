package jwhs.cheftoo.scrap.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import jwhs.cheftoo.recipe.dto.RecipeResponseDto;
import jwhs.cheftoo.recipe.entity.Recipe;
import jwhs.cheftoo.scrap.dto.ScrapInRecipeCheckAndCounDetailtDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ScrapInRecipeRepositoryCustom {
    Page<RecipeResponseDto> findRecipesInScrap(UUID scrapID, Pageable pageable);
    ScrapInRecipeCheckAndCounDetailtDto checkScrapAndCount(UUID recipeId, UUID memberId);
}
