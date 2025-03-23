package jwhs.cheftoo.recipe.dto;


import jwhs.cheftoo.recipe.entity.Recipe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRequestDto {

    private UUID memberId;
    private String recipeTitle;
    private String recipeContent;
    private List<String> ingredients; // 재료
    private List<String> cookingSteps; // 요리 순서
    private List<String> imgPaths; // 이미지 경로


}
