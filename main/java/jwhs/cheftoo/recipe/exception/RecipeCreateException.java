package jwhs.cheftoo.recipe.exception;


// 레시피 생성 예외
public class RecipeCreateException extends RuntimeException {
    public RecipeCreateException(String message) {
        super(message);
    }
}
