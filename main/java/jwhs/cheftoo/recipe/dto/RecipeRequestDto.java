package jwhs.cheftoo.recipe.dto;


import jwhs.cheftoo.cookingOrder.dto.CookingOrderDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRequestDto {
    private String recipeTitle;
    private String recipeContent;
    private List<RecipeDetailResponseDto.Ingredients> ingredients; // 재료
    private List<CookingOrderDto> cookingOrders; // 요리 순서
}
