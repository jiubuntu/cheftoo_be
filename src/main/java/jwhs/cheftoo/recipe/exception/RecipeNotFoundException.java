package jwhs.cheftoo.recipe.exception;

public class RecipeNotFoundException extends RuntimeException{

    public RecipeNotFoundException(String message) {
        super(message);
    }
}
