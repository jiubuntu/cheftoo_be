package jwhs.cheftoo.recipe.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jwhs.cheftoo.image.entity.QImages;
import jwhs.cheftoo.recipe.dto.RecipeResponseDto;
import jwhs.cheftoo.recipe.entity.QRecipe;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class RecipeRepositoryImpl implements RecipeRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<RecipeResponseDto> findAllWithImage(Pageable pageable) {
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
                .from(recipe)
                .leftJoin(images).on(images.recipe.eq(recipe))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(recipe.dataCreated.desc())
                .fetch();

        long total = queryFactory
                .select(recipe.count())
                .from(recipe)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
