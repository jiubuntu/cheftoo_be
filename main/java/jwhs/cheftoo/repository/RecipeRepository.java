package jwhs.cheftoo.repository;

import jwhs.cheftoo.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RecipeRepository extends JpaRepository<Recipe, UUID> {
    Optional<Recipe> findByRecipeId(UUID id);

}
