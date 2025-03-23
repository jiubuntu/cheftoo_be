package jwhs.cheftoo.recipe.dto;


import jwhs.cheftoo.recipe.entity.Recipe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {

    private UUID recipeId;
    private UUID memberId;
    private String recipeTitle;
    private String recipeContent;
    private String dataCreated;
    private String dataUpdated;


    // Entity를 Dto로 변환
    public static RecipeDto fromEntity(Recipe recipe) {
        return new RecipeDto(
                recipe.getRecipeId(),
                recipe.getMemberId(),
                recipe.getRecipeTitle(),
                recipe.getRecipeContent(),
                recipe.getDataCreated(),

                recipe.getDataUpdated()

        );
    }


}
