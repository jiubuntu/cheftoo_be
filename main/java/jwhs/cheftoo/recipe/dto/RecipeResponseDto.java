package jwhs.cheftoo.recipe.dto;


import jwhs.cheftoo.recipe.entity.Recipe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor

// 다건조회
public class RecipeResponseDto {

    private UUID recipeId;
    private UUID memberId;
    private String recipeTitle;
    private String recipeContent;
    private LocalDateTime dataCreated;
    private LocalDateTime dataUpdated;


    // Entity를 Dto로 변환
    public static RecipeResponseDto fromEntity(Recipe recipe) {
        return new RecipeResponseDto(
                recipe.getRecipeId(),
                recipe.getMemberId().getMemberId(),
                recipe.getRecipeTitle(),
                recipe.getRecipeContent(),
                recipe.getDataCreated(),

                recipe.getDataUpdated()

        );
    }
}


