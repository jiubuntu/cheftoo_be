package jwhs.cheftoo.recipe.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jwhs.cheftoo.cookingorder.dto.CookingOrderRequestSaveDto;
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
    private List<SauceRequestSaveDto> Sauce; // 소스
    private List<CookingOrderRequestSaveDto> cookingOrder; // 요리 순서
    private String recipeImageKey; // 레시피 이미지 객체 KEY
    private String recipeImageContentType; // 레시피 이미지 파일형식
}
