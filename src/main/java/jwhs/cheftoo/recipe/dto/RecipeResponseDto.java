package jwhs.cheftoo.recipe.dto;


import jwhs.cheftoo.recipe.entity.Recipe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

// 다건조회
public class RecipeResponseDto {

    private UUID recipeId;
    private UUID memberId;
    private String recipeTitle;
    private String recipeContent;
    private String nickName;
    private String imgPath;
    private LocalDateTime dataCreated;
    private LocalDateTime dataUpdated;

    public RecipeResponseDto(
            UUID recipeId,
            String recipeTitle,
            String recipeContent,
            LocalDateTime dataCreated
    ) {
        this.recipeId = recipeId;
        this.recipeTitle = recipeTitle;
        this.recipeContent = recipeContent;
        this.dataCreated = dataCreated;
    }


    // Entity를 Dto로 변환
    public static RecipeResponseDto fromEntity(Recipe recipe, String imgPath) {
        return new RecipeResponseDto(
                recipe.getRecipeId(),
                recipe.getMember().getMemberId(),
                recipe.getRecipeTitle(),
                recipe.getRecipeContent(),
                recipe.getMember().getNickname(),
                imgPath,
                recipe.getDataCreated(),
                recipe.getDataUpdated()

        );
    }
}


