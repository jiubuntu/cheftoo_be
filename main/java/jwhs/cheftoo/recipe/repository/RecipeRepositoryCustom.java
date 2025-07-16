package jwhs.cheftoo.recipe.repository;

import jwhs.cheftoo.recipe.dto.RecipeDetailResponseDto;
import jwhs.cheftoo.recipe.dto.RecipeResponseDto;
import jwhs.cheftoo.recipe.dto.RecipeWithImageDto;
import jwhs.cheftoo.recipe.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface RecipeRepositoryCustom {
    Page<RecipeResponseDto> findAllWithImage(Pageable pageable);
    List<RecipeResponseDto> findRecipesByViewsOrder(List<UUID> recipeIds);
    List<RecipeResponseDto> findRecipesDateOrder();

}
