package jwhs.cheftoo.comment.service;


import jakarta.transaction.Transactional;
import jwhs.cheftoo.auth.entity.Member;
import jwhs.cheftoo.auth.exception.MemberNotFoundException;
import jwhs.cheftoo.auth.repository.MemberRepository;
import jwhs.cheftoo.comment.dto.CommentRequestDto;
import jwhs.cheftoo.comment.dto.CommentResponseDto;
import jwhs.cheftoo.comment.dto.CommentSummaryDto;
import jwhs.cheftoo.comment.entity.Comment;
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

    private RecipeRepository recipeRepository;
    private MemberRepository memberRepository;
    private CommentRepository commentRepository;

    public CommentService(
            RecipeRepository recipeRepository,
            MemberRepository memberRepository,
            CommentRepository commentRepository
    ) {
        this.recipeRepository = recipeRepository;
        this.memberRepository = memberRepository;
        this.commentRepository = commentRepository;
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

        return CommentResponseDto.fromEntity(savedComment, comment.getMember().getNickname());
    }
}
