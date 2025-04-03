package jwhs.cheftoo.ingredient.repository;

import jwhs.cheftoo.ingredient.entity.Ingredients;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IngredientsRepository extends JpaRepository<Ingredients, UUID> {

    Optional<Ingredients> findByRecipeIdAndIngredientsName(UUID recipeId, String ingredientsName);
}
