package jwhs.cheftoo.global;

import jwhs.cheftoo.recipe.exception.RecipeCreateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;


//전역 예외 핸들러
@RestControllerAdvice
public class ExceptionHandler {

    @ExceptionHandler(RecipeCreateException.class)
    public ResponseEntity<Map<String, String>> handleRecipeException(RecipeCreateException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "레시피 등록 실패");
        error.put("message", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // or BAD_REQUEST
                .body(error);
    }
}
