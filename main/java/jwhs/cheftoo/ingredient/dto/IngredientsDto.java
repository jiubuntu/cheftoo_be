package jwhs.cheftoo.ingredient.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientsDto {
    private UUID ingredientsId;
    private UUID recipeId;
    private String ingredientsName;
    private String ingredientsNum;
}
