package jwhs.cheftoo.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jwhs.cheftoo.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class CommentResponseDto {
    private UUID commentId;
    private UUID recipeId;
    private String nickName;
    private String commentContent;
    @JsonFormat(pattern = "yy-MM-dd HH:mm")
    private LocalDateTime dataCreated;

    public static CommentResponseDto fromEntity(Comment comment, String nickName) {
        return CommentResponseDto.builder().
                commentId(comment.getCommentId())
                .recipeId(comment.getRecipe().getRecipeId())
                .nickName(nickName)
                .commentContent(comment.getCommentContent())
                .dataCreated(comment.getDataCreated())
                .build();
    }
}
