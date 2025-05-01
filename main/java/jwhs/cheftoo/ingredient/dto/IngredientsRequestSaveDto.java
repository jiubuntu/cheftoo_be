package jwhs.cheftoo.ingredient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientsRequestSaveDto {
    private String ingredientsName;
    private String ingredientsNum;
}