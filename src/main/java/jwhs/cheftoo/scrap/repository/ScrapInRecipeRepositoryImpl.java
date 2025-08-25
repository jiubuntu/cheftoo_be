package jwhs.cheftoo.scrap.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jwhs.cheftoo.image.entity.QImages;
import jwhs.cheftoo.image.enums.S3ImageType;
import jwhs.cheftoo.image.service.S3Service;
import jwhs.cheftoo.recipe.dto.RecipeResponseDto;
import jwhs.cheftoo.recipe.entity.QRecipe;
import jwhs.cheftoo.recipe.entity.Recipe;
import jwhs.cheftoo.scrap.dto.ScrapInRecipeCheckAndCounDetailtDto;
import jwhs.cheftoo.scrap.entity.QScrap;
import jwhs.cheftoo.scrap.entity.QScrapInRecipe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.net.URL;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class ScrapInRecipeRepositoryImpl implements ScrapInRecipeRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    private final S3Service s3Service;

    @Override
    public Page<RecipeResponseDto> findRecipesInScrap(UUID scrapId, Pageable pageable) {
        QScrapInRecipe scrapInRecipe = QScrapInRecipe.scrapInRecipe;
        QRecipe recipe = QRecipe.recipe;
        QImages images = QImages.images;


        List<RecipeResponseDto> content = queryFactory
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
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
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

        Long total = queryFactory
                .select(scrapInRecipe.recipe.count())
                .from(scrapInRecipe)
                .where(scrapInRecipe.scrap.scrapId.eq(scrapId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);

    }

    @Override
    public ScrapInRecipeCheckAndCounDetailtDto checkScrapAndCount(UUID recipeId, UUID memberId) {
        QScrapInRecipe scrapInRecipe = QScrapInRecipe.scrapInRecipe;
        QScrap scrap = QScrap.scrap;

        // 1) member가 스크랩한 scrapId (없으면 null)
        UUID scrapId = null;
        if (memberId != null) {
            scrapId = queryFactory
                    .select(scrap.scrapId)
                    .from(scrapInRecipe)
                    .join(scrapInRecipe.scrap, scrap)
                    .where(
                            scrapInRecipe.recipe.recipeId.eq(recipeId),
                            scrap.member.memberId.eq(memberId)
                    )
                    .fetchFirst(); // 유니크 제약이 있으면 한 건만 나옴
        }

        // 2) 전체 스크랩 수
        Long scrapCount = queryFactory
                .select(scrapInRecipe.count())
                .from(scrapInRecipe)
                .where(scrapInRecipe.recipe.recipeId.eq(recipeId))
                .fetchOne();

        return ScrapInRecipeCheckAndCounDetailtDto.builder()
                .scrap(scrapId != null)                 // 스크랩 여부
                .scrapId(scrapId)                       // 스크랩한 경우 해당 ID
                .scrapCount(scrapCount == null ? 0 : scrapCount)
                .build();
    }
}
