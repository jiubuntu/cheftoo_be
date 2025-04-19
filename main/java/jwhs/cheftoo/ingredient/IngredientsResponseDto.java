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

    public static IngredientsResponseDto fromEntity(Ingredients ingredients) {
        return IngredientsResponseDto.builder()
                .ingredientsId(ingredients.getIngredientsId())
                .recipeId(ingredients.getRecipeId())
                .ingredientsName(ingredients.getIngredientsName())
                .build();
    }
}
