package jwhs.cheftoo.scrap.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jwhs.cheftoo.image.entity.QImages;
import jwhs.cheftoo.image.enums.S3ImageType;
import jwhs.cheftoo.image.service.S3Service;
import jwhs.cheftoo.recipe.dto.RecipeResponseDto;
import jwhs.cheftoo.recipe.entity.QRecipe;
import jwhs.cheftoo.recipe.entity.Recipe;
import jwhs.cheftoo.scrap.entity.QScrapInRecipe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class ScrapInRecipeRepositoryImpl implements ScrapInRecipeRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    private final S3Service s3Service;

    @Override
    public List<RecipeResponseDto> findRecipesInScrap(UUID scrapId) {
        QScrapInRecipe scrapInRecipe = QScrapInRecipe.scrapInRecipe;
        QRecipe recipe = QRecipe.recipe;
        QImages images = QImages.images;

        return
                queryFactory
                        .select(Projections.constructor(RecipeResponseDto.class,
                                recipe.recipeId,
                                recipe.member.memberId,
                                recipe.recipeTitle,
                                recipe.recipeContent,
                                recipe.member.nickname,
                                images.imgPath,
                                recipe.dataCreated,
                                recipe.dataUpdated
                        ))
                        .from(scrapInRecipe)
                        .join(scrapInRecipe.recipe, recipe)
                        .leftJoin(images).on(images.recipe.eq(recipe))
                        .where(scrapInRecipe.scrap.scrapId.eq(scrapId))
                        .fetch()
                        .stream()
                        .map(dto -> {
                            String key = dto.getImgPath();
                            if (key != null && !key.isBlank()) {
                                try {
                                    URL presignedUrl = s3Service.generateRecipeImagePresignedGetUrl(key, S3ImageType.RECIPE_GET_DURATION);
                                    dto.setImgPath(presignedUrl.toString());
                                } catch ( Exception e ) {
                                    log.error("presignedUrl 발급 실패 - key: {}", key, e);
                                    dto.setImgPath(null);
                                }
                            }
                            return dto;
                        }).toList();

    }
}
