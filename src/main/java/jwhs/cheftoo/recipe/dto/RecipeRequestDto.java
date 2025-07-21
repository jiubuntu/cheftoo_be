package jwhs.cheftoo.recipe.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jwhs.cheftoo.cookingorder.dto.CookingOrderRequestSaveDto;
import jwhs.cheftoo.ingredient.dto.IngredientsRequestSaveDto;
import jwhs.cheftoo.sauce.dto.SauceRequestSaveDto;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeRequestDto {
    @JsonProperty("recipeTitle")
    private String recipeTitle;

    @JsonProperty("recipeContent")
    private String recipeContent;

    @JsonProperty("ingredients")
    private List<IngredientsRequestSaveDto> ingredients; // 재료

    @JsonProperty("sauce")
    private List<SauceRequestSaveDto> sauce; // 소스

    @JsonProperty("cookingOrder")
    private List<CookingOrderRequestSaveDto> cookingOrder; // 요리 순서

    @JsonProperty("recipeImageKey")
    private String recipeImageKey; // 레시피 이미지 객체 KEY

    @JsonProperty("recipeImageContentType")
    private String recipeImageContentType; // 레시피 이미지 파일형식
}
