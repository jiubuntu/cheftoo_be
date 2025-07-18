package jwhs.cheftoo.comment.exception;

public class CommentAccessDeniedException extends RuntimeException{
    public CommentAccessDeniedException(String message) {
        super(message);
    }
}
