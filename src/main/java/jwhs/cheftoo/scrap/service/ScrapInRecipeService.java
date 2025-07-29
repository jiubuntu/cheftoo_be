package jwhs.cheftoo.scrap.service;

import jakarta.transaction.Transactional;
import jwhs.cheftoo.recipe.dto.RecipeResponseDto;
import jwhs.cheftoo.recipe.entity.Recipe;
import jwhs.cheftoo.recipe.exception.RecipeNotFoundException;
import jwhs.cheftoo.recipe.repository.RecipeRepository;
import jwhs.cheftoo.scrap.dto.ScrapInRecipeCheckAndCounDetailtDto;
import jwhs.cheftoo.scrap.dto.ScrapInRecipeDeleteRequestDto;
import jwhs.cheftoo.scrap.dto.ScrapInRecipeResponseDto;
import jwhs.cheftoo.scrap.entity.Scrap;
import jwhs.cheftoo.scrap.entity.ScrapInRecipe;
import jwhs.cheftoo.scrap.exception.ScrapNotFoundException;
import jwhs.cheftoo.scrap.repository.ScrapInRecipeRepository;
import jwhs.cheftoo.scrap.repository.ScrapRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ScrapInRecipeService {
    private ScrapInRecipeRepository scrapInRecipeRepository;
    private ScrapRepository scrapRepository;
    private RecipeRepository recipeRepository;


    public ScrapInRecipeService(ScrapInRecipeRepository scrapInRecipeRepository, ScrapRepository scrapRepository, RecipeRepository recipeRepository) {
        this.scrapInRecipeRepository = scrapInRecipeRepository;
        this.scrapRepository = scrapRepository;
        this.recipeRepository = recipeRepository;
    }

    @Transactional
    public void saveScrapInRecipe(UUID scrapId, UUID recipeId) {

        Scrap scrap  = scrapRepository.findById(scrapId).orElseThrow(() -> {
            throw new ScrapNotFoundException("저장된 스크랩 폴더를 찾을 수 없습니다.");
        });

        Recipe recipe = recipeRepository.findByRecipeId(recipeId).orElseThrow(() -> {
            throw new RecipeNotFoundException("레시피를 찾을 수 없습니다.");
        });

        ScrapInRecipe scrapInRecipe = ScrapInRecipe.builder()
                .scrapInRecipeId(null)
                .scrap(scrap)
                .recipe(recipe)
                .build();

        scrapInRecipeRepository.save(scrapInRecipe);

    }

    // 특정 스크랩폴더의 모든 레시피 리스트를 가져오는 함수
    public ScrapInRecipeResponseDto findRecipeListByScrapId(UUID scrapId) {
        List<RecipeResponseDto> recipeList =  scrapInRecipeRepository.findRecipesInScrap(scrapId);

        return ScrapInRecipeResponseDto.builder()
                .scrapId(scrapId)
                .recipeList(recipeList)
                .build();
    }

    @Transactional
    public void deleteScrapInRecipeByScrapIdAndRecipeId(ScrapInRecipeDeleteRequestDto dto) {
        UUID scrapId = dto.getScrapId();
        List<UUID> recipeIdList = dto.getRecipeIdList();

        scrapInRecipeRepository.deleteByScrapIdAndRecipeId(scrapId, recipeIdList);
    }

    public ScrapInRecipeCheckAndCounDetailtDto checkBookMarkByRecipeAndMember(UUID recipeId, UUID memberId) {
        return scrapInRecipeRepository.checkScrapAndCount(recipeId, memberId);
    }

}
