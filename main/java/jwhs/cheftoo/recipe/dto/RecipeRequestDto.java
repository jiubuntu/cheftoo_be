package jwhs.cheftoo.recipe.dto;


import jwhs.cheftoo.cookingOrder.dto.CookingOrderDto;
import jwhs.cheftoo.ingredient.dto.IngredientsDto;
import jwhs.cheftoo.ingredient.dto.IngredientsRequestSaveDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeRequestDto {
    private String recipeTitle;
    private String recipeContent;
    private List<IngredientsRequestSaveDto> ingredients; // 재료
    private List<CookingOrderDto> cookingSteps; // 요리 순서
}
