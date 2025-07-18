package jwhs.cheftoo.image.dto;

import jwhs.cheftoo.image.entity.Images;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ImagesResponseDto {
    private UUID imageId;
    private UUID recipeId;
    private UUID memberId;
    private String imgPath;

    public static ImagesResponseDto fromEntity(Images images) {
        return ImagesResponseDto.builder()
                .imageId(images.getImageId())
                .recipeId(images.getRecipe().getRecipeId())
                .memberId(images.getMember().getMemberId())
                .imgPath(images.getImgPath())
                .build();
    }
}
