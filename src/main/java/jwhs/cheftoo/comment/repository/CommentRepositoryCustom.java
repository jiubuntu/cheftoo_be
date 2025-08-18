package jwhs.cheftoo.comment.repository;

import jwhs.cheftoo.auth.entity.Member;
import jwhs.cheftoo.comment.dto.CommentResponseDetailDto;
import jwhs.cheftoo.comment.dto.CommentResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.UUID;

public interface CommentRepositoryCustom {
    List<CommentResponseDto> findByRecipeId(UUID recipeId);
    Slice<CommentResponseDetailDto> findAllByMember(UUID memberId, Pageable pageable);
}
