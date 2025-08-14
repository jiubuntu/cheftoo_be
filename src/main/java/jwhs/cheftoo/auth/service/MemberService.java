package jwhs.cheftoo.auth.service;


import jakarta.transaction.Transactional;
import jwhs.cheftoo.auth.dto.MemberConsentRequestDto;
import jwhs.cheftoo.auth.entity.Member;
import jwhs.cheftoo.auth.entity.MemberConsent;
import jwhs.cheftoo.auth.exception.MemberDeleteException;
import jwhs.cheftoo.auth.exception.MemberNotFoundException;
import jwhs.cheftoo.auth.repository.MemberConsentRepository;
import jwhs.cheftoo.auth.repository.MemberRepository;
import jwhs.cheftoo.comment.service.CommentService;
import jwhs.cheftoo.recipe.entity.Recipe;
import jwhs.cheftoo.recipe.repository.RecipeRepository;
import jwhs.cheftoo.recipe.service.RecipeService;
import jwhs.cheftoo.scrap.service.ScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RecipeService recipeService;
    private final ScrapService scrapService;
    private final CommentService commentService;



    @Transactional
    public void updateNickname(UUID memberId, String nickname) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("해당 유저가 존재하지 않습니다."));
        member.setNickname(nickname);
        memberRepository.save(member);
    }


    public Member findMemberById(UUID memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> {
            throw new MemberNotFoundException("유저를 찾을 수 없습니다.");
        });
    }

    public String findNickNameByMemberId(UUID memberId) {
        return memberRepository.findNickNameByMemberId(memberId);
    }

    @Transactional
    public void deleteMember(UUID memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> {
            throw new NoSuchElementException("member를 찾을 수 없습니다,");
        });

        //// detail 먼저 삭제
        try {
            // recipe 삭제
            List<Recipe> recipeList = recipeService.findAllByMember(member);
            for (Recipe recipe : recipeList) {
                recipeService.deleteRecipe(recipe.getRecipeId());
            }
            // 스크랩 삭제
            scrapService.deleteAllByMember(member);
            // 댓글 삭제
            commentService.deleteAllByMember(member);

            //// header 삭제
            memberRepository.deleteById(member.getMemberId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new MemberDeleteException("회원탈퇴 중 에러가 발생했습니다.");
        }

    }


    public boolean checkNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }






}
