package jwhs.cheftoo.comment.dto;


import java.time.LocalDateTime;
import java.util.UUID;

public interface CommentSummaryDto {

    UUID getMemberId();
    String getCommentContent();
    LocalDateTime getDataCreated();
}
