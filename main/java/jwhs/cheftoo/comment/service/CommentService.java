package jwhs.cheftoo.comment.service;


import jwhs.cheftoo.auth.repository.MemberRepository;
import jwhs.cheftoo.comment.dto.CommentResponseDto;
import jwhs.cheftoo.comment.dto.CommentSummaryDto;
import jwhs.cheftoo.comment.entity.Comment;
import jwhs.cheftoo.comment.repository.CommentRepository;
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

    // 특정 레시피 모든 댓글 조회
    public List<CommentResponseDto> findAllCommentByRecipe(UUID recipeId) {
        Recipe recipe = recipeRepository.findByRecipeId(recipeId).orElseThrow(() -> {
            throw new RecipeNotFoundException("레시피를 찾을 수 없습니다.");
        } );

        // 댓글 가져오기
        List<CommentSummaryDto> commentList =  commentRepository.findAllByRecipe(recipe);
        for(CommentSummaryDto comment : commentList) {
            // 닉네임 찾기
            UUID memberId = comment.getMemberId();
            String nickName = memberRepository.findNickNameByMemberId(memberId);



        }



    }
}
