package jwhs.cheftoo.ingredient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientsRequestSaveDto {
    @JsonProperty("ingredientsName")
    private String ingredientsName;

    @JsonProperty("ingredientsNum")
    private String ingredientsNum;

}