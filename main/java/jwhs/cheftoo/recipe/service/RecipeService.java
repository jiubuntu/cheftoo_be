package jwhs.cheftoo.recipe.service;

import jakarta.transaction.Transactional;
import jwhs.cheftoo.image.service.ImageService;
import jwhs.cheftoo.recipe.dto.CookingStepsDto;
import jwhs.cheftoo.recipe.dto.RecipeRequestDto;
import jwhs.cheftoo.recipe.entity.CookingOrder;
import jwhs.cheftoo.recipe.entity.Ingredients;
import jwhs.cheftoo.recipe.entity.Recipe;
import jwhs.cheftoo.recipe.dto.RecipeDto;
import jwhs.cheftoo.recipe.exception.RecipeCreateException;
import jwhs.cheftoo.recipe.repository.CookingOrderRepository;
import jwhs.cheftoo.recipe.repository.IngredientsRepository;
import jwhs.cheftoo.recipe.repository.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private RecipeRepository recipeRepository;
    private IngredientsRepository ingredientsRepository;
    private CookingOrderRepository cookingOrderRepository;
    private ImageService imageService;

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

    @Transactional
    public UUID createRecipe(RecipeRequestDto recipeRequestDto, String memberId, MultipartFile imageFile, List<MultipartFile> stepImages) {
        try {
            // 1. 레시피 저장
            Recipe recipe = Recipe.builder()
                    .memberId(UUID.fromString(memberId))
                    .recipeTitle(recipeRequestDto.getRecipeTitle())
                    .recipeContent(recipeRequestDto.getRecipeContent())
                    .build();
            UUID recipeId = recipeRepository.save(recipe).getRecipeId();

            // 2. 대표 이미지 저장
            if (imageFile != null && !imageFile.isEmpty()) {
                imageService.saveMainImage(imageFile, UUID.fromString(memberId), recipeId);
            }

            // 3. 재료 저장
            if (recipeRequestDto.getIngredients().size() > 0) {
                List<Ingredients> ingredients = recipeRequestDto.getIngredients().stream()
                        .map( ingredient -> {
                            return Ingredients.builder()
                                    .ingredientsName(ingredient)
                                    .build();
                        })
                        .collect(Collectors.toList());
                ingredientsRepository.saveAll(ingredients);
            }

            // 4. 조리순서 저장
            List<CookingStepsDto> steps = recipeRequestDto.getCookingSteps();
            if (steps.size() > 0) {
                int idx = 1;
                for (int i = 0; i < steps.size(); i++) {
                    String content = steps.get(i).getContent();
                    MultipartFile image = stepImages.get(i);
                    String imagePath = imageService.saveCookingOrderImage(image);

                    // 저장 로직: content + image 조합
                    cookingOrderRepository.save(
                            CookingOrder.builder()
                                    .recipeId(recipeId)
                                    .order(idx)
                                    .content(content)
                                    .imgPath(imagePath)
                                    .build()
                    );

                    idx ++;
                }
            }
            return recipeId;
        } catch ( RecipeCreateException e) {
            throw new RecipeCreateException("레시피 저장 중 에러 발생");
        }


    }


}
