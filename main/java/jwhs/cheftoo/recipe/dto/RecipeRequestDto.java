package jwhs.cheftoo.recipe.dto;


import jwhs.cheftoo.recipe.entity.Recipe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRequestDto {
    private String recipeTitle;
    private String recipeContent;
    private List<String> ingredients; // 재료
    private List<CookingStepsDto> cookingSteps; // 요리 순서
}
