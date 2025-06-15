package jwhs.cheftoo.scrap.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jwhs.cheftoo.recipe.dto.RecipeResponseDto;
import jwhs.cheftoo.recipe.entity.QRecipe;
import jwhs.cheftoo.recipe.entity.Recipe;
import jwhs.cheftoo.scrap.entity.QScrapInRecipe;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ScrapInRecipeRepositoryImpl implements ScrapInRecipeRepositoryCustom{
    private final JPAQueryFactory queryFactory;


    @Override
    public List<RecipeResponseDto> findRecipesInScrap(UUID scrapID) {
        QScrapInRecipe scrapInRecipe = QScrapInRecipe.scrapInRecipe;
        QRecipe recipe = QRecipe.recipe;

        return queryFactory
                .select(Projections.constructor(RecipeResponseDto.class))
                .from(scrapInRecipe)
                .join(scrapInRecipe.recipe, recipe)
                .where(scrapInRecipe.scrap.scrapId.eq(scrapID))
                .fetch();

    }
}
