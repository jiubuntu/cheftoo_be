package jwhs.cheftoo.comment.service;


import ch.qos.logback.core.encoder.EchoEncoder;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jwhs.cheftoo.auth.entity.Member;
import jwhs.cheftoo.auth.port.MemberReader;
import jwhs.cheftoo.comment.dto.CommentRequestSaveDto;
import jwhs.cheftoo.comment.dto.CommentRequestUpdateDto;
import jwhs.cheftoo.comment.dto.CommentResponseDto;
import jwhs.cheftoo.comment.entity.Comment;
import jwhs.cheftoo.comment.exception.CommentNotFoundException;
import jwhs.cheftoo.comment.exception.CommentSaveException;
import jwhs.cheftoo.comment.repository.CommentRepository;
import jwhs.cheftoo.recipe.entity.Recipe;
import jwhs.cheftoo.recipe.exception.RecipeNotFoundException;
import jwhs.cheftoo.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final EntityManager entityManager;
    private final RecipeRepository recipeRepository;
    private final MemberReader memberReader;
    private final CommentRepository commentRepository;


    //특정 레시피 모든 댓글 조회
    public List<CommentResponseDto> findAllCommentByRecipe(UUID recipeId) {
        Recipe recipe = recipeRepository.findByRecipeId(recipeId).orElseThrow(() -> {
            throw new RecipeNotFoundException("레시피를 찾을 수 없습니다.");
        } );

        return commentRepository.findByRecipeId(recipeId);

    }

    public List<CommentResponseDto> findAllCommentByMember(UUID memberId) {
        Member member = memberReader.getById(memberId);
        String nickName = member.getNickname();

        List<CommentResponseDto> commentList = commentRepository.findAllByMember(member).stream()
                .map(comment -> CommentResponseDto.fromEntity(comment, nickName))
                .toList();

        return commentList;
    }

    // 댓글 저장
    @Transactional
    public CommentResponseDto createComment(CommentRequestSaveDto dto, UUID memberId, UUID recipeId) {
        return saveComment(null,dto, memberId, recipeId);
    }


    private CommentResponseDto saveComment(UUID commentId, CommentRequestSaveDto dto, UUID memberId, UUID recipeId) {
        String commentContent = dto.getCommentContent();

        Member member = memberReader.getById(memberId);

        Recipe recipe = recipeRepository.findByRecipeId(recipeId).orElseThrow(() -> {
            throw new RecipeNotFoundException("레시피를 찾을 수 없습니다.");
        });

        Comment comment = Comment.builder()
                .commentId(null)
                .recipe(recipe)
                .member(member)
                .commentContent(commentContent)
                .build();

        Comment savedComment = commentRepository.save(comment);
        // dataCreated timeStamp 컬럼은 flush를 해야 DB에서 값을 넣기때문에, 수동 flush 후, DB데이터와 동기화
        entityManager.flush();
        entityManager.refresh(savedComment);

        return CommentResponseDto.fromEntity(savedComment, comment.getMember().getNickname());
    }


    @Transactional
    public void deleteComment(UUID commentId, UUID memberId) throws AccessDeniedException {
        // 댓글을 작성한 멤버인지 확인
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {
            throw new CommentNotFoundException("댓글을 찾을 수 없습니다.");
        });

        if (!comment.getMember().getMemberId().equals(memberId)) {
            throw new AccessDeniedException("로그인된 유저와 댓글 작성자가 일치하지 않습니다.");
        }

        commentRepository.deleteById(commentId);

    }

    @Transactional
    public void deleteAllByMember(Member member) {
        commentRepository.deleteAllByMember(member);
    }

    @Transactional
    public void updateComment(CommentRequestUpdateDto dto) {
        UUID commentId = dto.getCommentId();
        String newCommentContent = dto.getCommentContent();

        Comment savedComment = commentRepository.findById(commentId).orElseThrow(() -> {
            throw new CommentNotFoundException("댓글을 찾을 수 없습니다");
        });

        try {
            savedComment.setCommentContent(newCommentContent);
            commentRepository.save(savedComment);
        } catch (Exception e) {
            throw new CommentSaveException("댓글 저장 중 문제가 발생했습니다.");
        }



    }
}
