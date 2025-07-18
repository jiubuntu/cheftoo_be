package jwhs.cheftoo.ingredient;

import jwhs.cheftoo.ingredient.entity.Ingredients;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class IngredientsResponseDto {
    private UUID ingredientsId;
    private UUID recipeId;
    private String ingredientsName;
    private String ingredientsNum;

    public static IngredientsResponseDto fromEntity(Ingredients ingredients) {
        return IngredientsResponseDto.builder()
                .ingredientsId(ingredients.getIngredientsId())
                .recipeId(ingredients.getRecipe().getRecipeId())
                .ingredientsName(ingredients.getIngredientsName())
                .ingredientsNum(ingredients.getIngredientsNum())
                .build();
    }
}
