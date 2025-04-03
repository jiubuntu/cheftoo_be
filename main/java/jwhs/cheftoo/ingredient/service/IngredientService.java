package jwhs.cheftoo.ingredient.service;

import jwhs.cheftoo.ingredient.entity.Ingredients;
import jwhs.cheftoo.ingredient.repository.IngredientsRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class IngredientService {

    private IngredientsRepository ingredientsRepository;

    public IngredientService(IngredientsRepository ingredientsRepository) {
        this.ingredientsRepository = ingredientsRepository;
    }

    public Ingredients findByRecipeIdAndIngredientsName(UUID recipeId, String ingedientsName) {
        Ingredients ingredients = ingredientsRepository.findByRecipeIdAndIngredientsName(recipeId, ingedientsName)
                .orElseGet(null);
        return ingredients;
    }
}
