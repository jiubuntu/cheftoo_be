package jwhs.cheftoo.recipe.dto;
import lombok.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


// 단건조회
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDetailResponseDto {
    private UUID recipeId;
    private UUID memberId;
    private String recipeTitle;
    private String recipeContent;
    private Images images;
    private Ingredients ingredients;
    private List<CookingOrder> cookingOrder;

    @Getter
    @Builder
    public static class Images {
        private UUID imageId;
        private UUID recipeId;
        private UUID memberId;
        private String imgPath;

        public static Images fromEntity(jwhs.cheftoo.image.entity.Images imagesEntity) {
            return Images.builder()
                    .imageId(imagesEntity.getImageId())
                    .recipeId(imagesEntity.getRecipe().getRecipeId())
                    .memberId(imagesEntity.getMember().getMemberId())
                    .imgPath(imagesEntity.getImgPath())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class Ingredients {
        private UUID ingredientsId;
        private UUID recipeId;
        private String ingredientsName;
        private String ingredientsNum;

        public static Ingredients fromEntity(jwhs.cheftoo.ingredient.entity.Ingredients ingredientsEntity) {
            return Ingredients.builder()
                    .ingredientsId(ingredientsEntity.getIngredientsId())
                    .recipeId(ingredientsEntity.getRecipe().getRecipeId())
                    .ingredientsName(ingredientsEntity.getIngredientsName())
                    .ingredientsNum(ingredientsEntity.getIngredientsNum())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class CookingOrder {
        private UUID cookingOrderId;
        private UUID recipeId;
        private long order;
        private String content;
        private String imgPath;

        public static List<CookingOrder> fromEntity(List<jwhs.cheftoo.cookingorder.entity.CookingOrder> cookingOrderEntitys) {
            return cookingOrderEntitys.stream()
                            .map(cookingOrderEntity -> {
                                return CookingOrder.builder()
                                        .cookingOrderId(cookingOrderEntity.getCookingOrderId())
                                        .recipeId(cookingOrderEntity.getRecipe().getRecipeId())
                                        .order(cookingOrderEntity.getOrder())
                                        .content(cookingOrderEntity.getContent())
                                        .imgPath(cookingOrderEntity.getImgPath())
                                        .build();
                            })
                    .collect(Collectors.toList());
        }
    }




}
