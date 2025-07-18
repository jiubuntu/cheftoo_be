package jwhs.cheftoo.recipe.exception;


// 레시피 등록 중 에러 발생 예외
public class RecipeCreateException extends RuntimeException {
    public RecipeCreateException(String message) {
        super(message);
    }
}
