package jwhs.cheftoo.comment.service;


import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jwhs.cheftoo.auth.entity.Member;
import jwhs.cheftoo.auth.exception.MemberNotFoundException;
import jwhs.cheftoo.auth.repository.MemberRepository;
import jwhs.cheftoo.comment.dto.CommentRequestDto;
import jwhs.cheftoo.comment.dto.CommentResponseDto;
import jwhs.cheftoo.comment.dto.CommentSummaryDto;
import jwhs.cheftoo.comment.entity.Comment;
import jwhs.cheftoo.comment.exception.CommentAccessDeniedException;
import jwhs.cheftoo.comment.exception.CommentNotFoundException;
import jwhs.cheftoo.comment.repository.CommentRepository;
import jwhs.cheftoo.comment.repository.CommentRepositoryCustom;
import jwhs.cheftoo.recipe.entity.Recipe;
import jwhs.cheftoo.recipe.exception.RecipeNotFoundException;
import jwhs.cheftoo.recipe.repository.RecipeRepository;
import jwhs.cheftoo.recipe.service.RecipeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class CommentService {

    private EntityManager entityManager;
    private RecipeRepository recipeRepository;
    private MemberRepository memberRepository;
    private CommentRepository commentRepository;

    public CommentService(
            RecipeRepository recipeRepository,
            MemberRepository memberRepository,
            CommentRepository commentRepository,
            EntityManager entityManager
    ) {
        this.recipeRepository = recipeRepository;
        this.memberRepository = memberRepository;
        this.commentRepository = commentRepository;
        this.entityManager = entityManager;
    }

    //특정 레시피 모든 댓글 조회
    public List<CommentResponseDto> findAllCommentByRecipe(UUID recipeId) {
        Recipe recipe = recipeRepository.findByRecipeId(recipeId).orElseThrow(() -> {
            throw new RecipeNotFoundException("레시피를 찾을 수 없습니다.");
        } );

        return commentRepository.findByRecipeId(recipeId);

    }

    // 댓글 저장
    @Transactional
    public CommentResponseDto createComment(CommentRequestDto dto, UUID memberId, UUID recipeId) {
        return saveComment(null,dto, memberId, recipeId);
    }


    private CommentResponseDto saveComment(UUID commentId, CommentRequestDto dto, UUID memberId, UUID recipeId) {
        String commentContent = dto.getCommentContent();

        Member member = memberRepository.findById(memberId).orElseThrow(() -> {
            throw new MemberNotFoundException("댓글을 작성한 유저를 찾을 수 없습니다.");
        });

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
    public void deleteComment(UUID commentId, UUID memberId) {
        // 댓글을 작성한 멤버인지 확인
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {
            throw new CommentNotFoundException("댓글을 찾을 수 없습니다.");
        });

        if (!comment.getMember().getMemberId().equals(memberId)) {
            throw new CommentAccessDeniedException("로그인된 유저와 댓글 작성자가 일치하지 않습니다.");
        }

        commentRepository.deleteById(commentId);

    }
}
