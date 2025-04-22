package jwhs.cheftoo.ingredient.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Builder
public class IngredientsRequestDto {
    private UUID ingredientsId;
    private UUID recipeId;
    private String ingredientsName;
    private String ingredientsNum;
}
