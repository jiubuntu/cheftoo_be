package jwhs.cheftoo.ingredient.repository;

import jwhs.cheftoo.ingredient.entity.Ingredients;
import jwhs.cheftoo.recipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IngredientsRepository extends JpaRepository<Ingredients, UUID> {

    Optional<Ingredients> findByRecipeAndIngredientsName(Recipe recipe, String ingredientsName);
    Optional<Ingredients> findByRecipe(Recipe recipe);
    void deleteByRecipe(Recipe recipe);
}
