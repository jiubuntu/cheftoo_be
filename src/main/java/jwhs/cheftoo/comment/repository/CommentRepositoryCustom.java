package jwhs.cheftoo.comment.repository;

import jwhs.cheftoo.auth.entity.Member;
import jwhs.cheftoo.comment.dto.CommentResponseDetailDto;
import jwhs.cheftoo.comment.dto.CommentResponseDto;

import java.util.List;
import java.util.UUID;

public interface CommentRepositoryCustom {
    List<CommentResponseDto> findByRecipeId(UUID recipeId);
    List<CommentResponseDetailDto> findAllByMember(UUID memberId);
}
