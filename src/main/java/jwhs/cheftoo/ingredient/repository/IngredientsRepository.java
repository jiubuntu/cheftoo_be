package jwhs.cheftoo.ingredient.repository;

import jwhs.cheftoo.ingredient.entity.Ingredients;
import jwhs.cheftoo.recipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IngredientsRepository extends JpaRepository<Ingredients, UUID> {

    Optional<Ingredients> findByRecipeAndIngredientsName(Recipe recipe, String ingredientsName);
    List<Ingredients> findAllByRecipe(Recipe recipe);
    void deleteByRecipe(Recipe recipe);
}
