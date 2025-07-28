package jwhs.cheftoo.recipe.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Nullable;
import jwhs.cheftoo.cookingorder.entity.QCookingOrder;
import jwhs.cheftoo.image.entity.QImages;
import jwhs.cheftoo.image.enums.S3ImageType;
import jwhs.cheftoo.image.service.S3Service;
import jwhs.cheftoo.ingredient.entity.QIngredients;
import jwhs.cheftoo.recipe.dto.RecipeDetailResponseDto;
import jwhs.cheftoo.recipe.dto.RecipeResponseDto;
import jwhs.cheftoo.recipe.dto.RecipeWithImageDto;
import jwhs.cheftoo.recipe.entity.QRecipe;
import jwhs.cheftoo.recipe.entity.Recipe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
@Slf4j
public class RecipeRepositoryImpl implements RecipeRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final S3Service s3Service;

    @Override
    public Page<RecipeResponseDto> findAllWithImage(Pageable pageable, @Nullable String keyword) {
        QRecipe recipe = QRecipe.recipe;
        QImages images = QImages.images;


        BooleanBuilder where = new BooleanBuilder();
        if (keyword != null && !keyword.isBlank()) {
            where.and(recipe.recipeTitle.containsIgnoreCase(keyword));
        }

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
                .from(recipe)
                .leftJoin(images).on(images.recipe.eq(recipe))
                .where(where)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(recipe.dataCreated.desc())
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
                })
                .toList();

        long total = queryFactory
                .select(recipe.count())
                .from(recipe)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    public List<RecipeResponseDto> findRecipesByViewsOrder(List<UUID> recipeIds) {
        QRecipe recipe = QRecipe.recipe;
        QImages images = QImages.images;

        CaseBuilder caseBuilder = new CaseBuilder();
        CaseBuilder.Cases<Integer, NumberExpression<Integer>> cases = null;

        // case when 절 만들기
        for (int i = 0; i < recipeIds.size(); i++) {
            cases = (cases == null)
                    ? caseBuilder.when(recipe.recipeId.eq(recipeIds.get(i))).then(i)
                    : cases.when(recipe.recipeId.eq(recipeIds.get(i))).then(i);
        }

        NumberExpression<Integer> sortOrder = cases.otherwise(Integer.MAX_VALUE);

        return
                queryFactory
                        .select(Projections.constructor(RecipeResponseDto.class,
                                recipe.recipeId,
                                recipe.recipeTitle,
                                recipe.recipeContent,
                                images.imgPath,
                                recipe.dataCreated
                        ))
                        .from(recipe)
                        .leftJoin(images).on(images.recipe.eq(recipe))
                        .where(recipe.recipeId.in(recipeIds))
                        .orderBy(sortOrder.asc()) // Redis 인기순 정렬 유지
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
                        })
                        .toList();

    }

    @Override
    public List<RecipeResponseDto> findRecipesDateOrder() {
        QRecipe recipe = QRecipe.recipe;
        QImages images = QImages.images;

        return
                queryFactory.select(Projections.constructor(RecipeResponseDto.class,
                recipe.recipeId,
                recipe.member.memberId,
                recipe.recipeTitle,
                recipe.recipeContent,
                recipe.member.nickname,
                images.imgPath,
                recipe.dataCreated,
                recipe.dataUpdated
                ))
                .from(recipe)
                .leftJoin(images).on(images.recipe.eq(recipe))
                .limit(10)
                .orderBy(recipe.dataCreated.desc())
                .fetch()
                .stream()
                .map(dto -> {
                    String key = dto.getImgPath();
                    if (key != null && !key.isBlank()) {
                        try {
                            URL presignedUrl = s3Service.generateRecipeImagePresignedGetUrl(key, S3ImageType.RECIPE_GET_DURATION);
                            dto.setImgPath(presignedUrl.toString());
                        } catch ( Exception e ) {
                            log.error("[presignedUrl 발급 실패] Key: {}", key, e);
                            dto.setImgPath(null);
                        }
                    }
                    return dto;
                }).toList();
    }


}
