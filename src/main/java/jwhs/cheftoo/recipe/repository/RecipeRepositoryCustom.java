package jwhs.cheftoo.recipe.repository;

import jakarta.annotation.Nullable;
import jwhs.cheftoo.auth.entity.Member;
import jwhs.cheftoo.recipe.dto.RecipeDetailResponseDto;
import jwhs.cheftoo.recipe.dto.RecipeResponseDto;
import jwhs.cheftoo.recipe.dto.RecipeWithImageDto;
import jwhs.cheftoo.recipe.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface RecipeRepositoryCustom {
    Page<RecipeResponseDto> findAllWithImage(Pageable pageable, @Nullable String keyword);
    List<RecipeResponseDto> findRecipesByViewsOrder(List<UUID> recipeIds);
    List<RecipeResponseDto> findRecipesDateOrder();

    List<RecipeResponseDto> findAllByMember(Member member);


}
