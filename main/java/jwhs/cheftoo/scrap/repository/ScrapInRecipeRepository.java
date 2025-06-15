package jwhs.cheftoo.scrap.repository;

import jwhs.cheftoo.comment.repository.CommentRepositoryCustom;
import jwhs.cheftoo.scrap.entity.ScrapInRecipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ScrapInRecipeRepository extends JpaRepository<ScrapInRecipe, UUID>, ScrapInRecipeRepositoryCustom {

}
