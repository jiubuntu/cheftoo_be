package jwhs.cheftoo.comment.repository;

import jwhs.cheftoo.auth.entity.Member;
import jwhs.cheftoo.comment.dto.CommentSummaryDto;
import jwhs.cheftoo.comment.entity.Comment;
import jwhs.cheftoo.recipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID>, CommentRepositoryCustom {

    void deleteAllByMember(Member member);
    List<Comment> findAllByMember(Member member);
}
