package jwhs.cheftoo.scrap.repository;

import jwhs.cheftoo.comment.repository.CommentRepositoryCustom;
import jwhs.cheftoo.scrap.entity.ScrapInRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ScrapInRecipeRepository extends JpaRepository<ScrapInRecipe, UUID>, ScrapInRecipeRepositoryCustom {


    @Modifying
    @Query("DELETE FROM ScrapInRecipe s WHERE s.scrap.scrapId = :scrapId AND s.recipe.recipeId IN :recipeIdList")
    void deleteByScrapIdAndRecipeId(UUID scrapId, List<UUID> recipeIdList);
}
