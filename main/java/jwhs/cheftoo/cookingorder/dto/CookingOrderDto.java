package jwhs.cheftoo.cookingorder.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CookingOrderDto {
    private UUID cookingOrderId;
    private UUID recipeId;
    private long order;
    private String content;
    private String imgPath;
}
