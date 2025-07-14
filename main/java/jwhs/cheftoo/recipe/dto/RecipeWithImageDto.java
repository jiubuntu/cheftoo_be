package jwhs.cheftoo.recipe.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class RecipeWithImageDto {
    private UUID recipeId;
    private UUID memberId;
    private String recipeTitle;
    private String recipeContent;
    private String imgPath;
}
