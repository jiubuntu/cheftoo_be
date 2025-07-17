package jwhs.cheftoo.global;

import io.lettuce.core.RedisException;
import jwhs.cheftoo.auth.exception.MemberNotFoundException;
import jwhs.cheftoo.comment.exception.CommentAccessDeniedException;
import jwhs.cheftoo.comment.exception.CommentNotFoundException;
import jwhs.cheftoo.recipe.exception.RecipeCreateException;
import jwhs.cheftoo.recipe.exception.RecipeFindException;
import jwhs.cheftoo.recipe.exception.RecipeNotFoundException;
import jwhs.cheftoo.scrap.exception.ScrapNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
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

    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleRecipeNotFoundException(RecipeNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "레시피를 찾을 수 없습니다.");
        error.put("message", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
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

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCommentNotFoundException(CommentNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "CommentNotFoundException");
        error.put("message", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // or BAD_REQUEST
                .body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "AccessDeniedException");
        error.put("message", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(error);
    }

    @ExceptionHandler(ScrapNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleScrapNotFoundException(ScrapNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "ScrapNotFoundException");
        error.put("message", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(RecipeFindException.class)
    public ResponseEntity<Map<String, String>> handleRecipeFindException(RecipeFindException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "RecipeFindException");
        error.put("message", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    @ExceptionHandler(RedisException.class)
    public ResponseEntity<Map<String, String>> handleRedisException(RedisException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "RedisException");
        error.put("message", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }


}
