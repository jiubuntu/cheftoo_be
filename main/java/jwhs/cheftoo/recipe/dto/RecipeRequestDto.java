package jwhs.cheftoo.recipe.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jwhs.cheftoo.cookingOrder.dto.CookingOrderDto;
import jwhs.cheftoo.cookingOrder.dto.CookingOrderRequestSaveDto;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecipeRequestDto {
    private String recipeTitle;
    private String recipeContent;
    private List<IngredientsRequestSaveDto> ingredients; // 재료
    private List<CookingOrderRequestSaveDto> cookingOrders; // 요리 순서
}
