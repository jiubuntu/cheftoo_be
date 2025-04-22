package jwhs.cheftoo.ingredient.dto;


import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class IngredientsDto {
    private UUID ingredientsId;
    private UUID recipeId;
    private String ingredientsName;
    private String ingredientsNum;
}
