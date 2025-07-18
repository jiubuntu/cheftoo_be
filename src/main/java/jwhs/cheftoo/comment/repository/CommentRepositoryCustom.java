package jwhs.cheftoo.comment.repository;

import jwhs.cheftoo.comment.dto.CommentResponseDto;

import java.util.List;
import java.util.UUID;

public interface CommentRepositoryCustom {
    List<CommentResponseDto> findByRecipeId(UUID recipeId);
}
