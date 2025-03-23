package jwhs.cheftoo.recipe.service;

import jakarta.transaction.Transactional;
import jwhs.cheftoo.recipe.dto.RecipeRequestDto;
import jwhs.cheftoo.recipe.entity.Recipe;
import jwhs.cheftoo.recipe.dto.RecipeDto;
import jwhs.cheftoo.recipe.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }


    // 단건조회
    public RecipeDto findRecipeById(UUID recipeId) {
        Recipe recipe = recipeRepository.findByRecipeId(recipeId)
                .orElseThrow(() -> new NoSuchElementException("해당되는 레시피를 찾을 수 없습니다."));

        return RecipeDto.fromEntity(recipe);
    }

    // 전체조회
    public List<RecipeDto> getAllRecipes() {
        List<Recipe> recipeList = recipeRepository.findAll();

        return recipeList.stream()
                .map(RecipeDto :: fromEntity)
                .collect(Collectors.toList());
    }

//    @Transactional
//    public UUID createRecipe(RecipeRequestDto recipeRequestDto, String memberId) {
//        // 1. 레시피 저장
//        Recipe recipe = Recipe.builder()
//                .memberId(UUID.fromString(memberId))
//                .recipeTitle(recipeRequestDto.getRecipeTitle())
//                .recipeContent(recipeRequestDto.getRecipeContent())
//                .build();
//        recipeRepository.save(recipe);
//
//        // 2. 재료 저장
//
//    }


}
