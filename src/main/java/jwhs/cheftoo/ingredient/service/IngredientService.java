package jwhs.cheftoo.ingredient.service;

import jwhs.cheftoo.ingredient.entity.Ingredients;
import jwhs.cheftoo.ingredient.repository.IngredientsRepository;
import jwhs.cheftoo.recipe.entity.Recipe;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class IngredientService {

    private IngredientsRepository ingredientsRepository;

    public IngredientService(IngredientsRepository ingredientsRepository) {
        this.ingredientsRepository = ingredientsRepository;
    }

    public Ingredients findByRecipeAndIngredientsName(Recipe recipe, String ingedientsName) {
        Ingredients ingredients = ingredientsRepository.findByRecipeAndIngredientsName(recipe, ingedientsName)
                .orElseGet(null);
        return ingredients;
    }
}
