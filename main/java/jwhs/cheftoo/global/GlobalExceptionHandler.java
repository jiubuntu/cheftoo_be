package jwhs.cheftoo.global;

import jwhs.cheftoo.auth.exception.MemberNotFoundException;
import jwhs.cheftoo.recipe.exception.RecipeCreateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;


//전역 예외 핸들러
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(RecipeCreateException.class)
    public ResponseEntity<Map<String, String>> handleRecipeException(RecipeCreateException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "레시피 등록 실패");
        error.put("message", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // or BAD_REQUEST
                .body(error);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleMemberNotFoundException(MemberNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "멤버 조회 실패");
        error.put("message", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // or BAD_REQUEST
                .body(error);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNoSuchElementException(NoSuchElementException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "NoSuchElementException");
        error.put("message", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // or BAD_REQUEST
                .body(error);
    }


}
