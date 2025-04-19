package jwhs.cheftoo.recipe.service;

import jakarta.transaction.Transactional;
import jwhs.cheftoo.cookingOrder.entity.CookingOrder;
import jwhs.cheftoo.image.service.ImageService;
import jwhs.cheftoo.cookingOrder.dto.CookingOrderDto;
import jwhs.cheftoo.recipe.dto.RecipeDetailResponseDto;
import jwhs.cheftoo.recipe.dto.RecipeRequestDto;
import jwhs.cheftoo.ingredient.entity.Ingredients;
import jwhs.cheftoo.recipe.entity.Recipe;
import jwhs.cheftoo.recipe.dto.RecipeResponseDto;
import jwhs.cheftoo.recipe.exception.RecipeCreateException;
import jwhs.cheftoo.cookingOrder.repository.CookingOrderRepository;
import jwhs.cheftoo.ingredient.repository.IngredientsRepository;
import jwhs.cheftoo.recipe.repository.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
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


    public RecipeService(RecipeRepository recipeRepository, IngredientsRepository ingredientsRepository, CookingOrderRepository cookingOrderRepository, ImageService imageService) {
        this.recipeRepository = recipeRepository;
        this.ingredientsRepository = ingredientsRepository;
        this.cookingOrderRepository = cookingOrderRepository;
        this.imageService = imageService;
    }

    // 단건조회(상세조회)
    @Transactional
    public RecipeDetailResponseDto findRecipeByRecipeId(UUID recipeId) {
        // 레시피 조회
        Recipe recipe = recipeRepository.findByRecipeId(recipeId)
                .orElseThrow(() -> new NoSuchElementException("해당되는 레시피를 찾을 수 없습니다."));
        // 대표 이미지 조회
        RecipeDetailResponseDto.Images images = RecipeDetailResponseDto.Images.fromEntity(imageService.findMainImageByRecipeId(recipeId));

        // 재료 조회
        RecipeDetailResponseDto.Ingredients ingredients =
                RecipeDetailResponseDto.Ingredients.fromEntity(ingredientsRepository.findByRecipeId(recipeId).orElseThrow(() -> new NoSuchElementException("레시피에 해당하는 재료를 찾을 수 없습니다.")));

        // 조리순서 조회
        List<RecipeDetailResponseDto.CookingOrder> cookingOrder = RecipeDetailResponseDto.CookingOrder.fromEntity(cookingOrderRepository.findByRecipeIdOrderByOrderDesc(recipeId));

        return RecipeDetailResponseDto.builder()
                .recipeId(recipe.getRecipeId())
                .memberId(recipe.getMemberId())
                .recipeTitle(recipe.getRecipeTitle())
                .recipeContent(recipe.getRecipeContent())
                .images(images)
                .ingredients(ingredients)
                .cookingOrder(cookingOrder)
                .build();
    }


    // 전체조회
    public List<RecipeResponseDto> findAllRecipes() {
        List<Recipe> recipeList = recipeRepository.findAll();

        return recipeList.stream()
                .map(RecipeResponseDto:: fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public UUID createRecipe(RecipeRequestDto recipeRequestDto, UUID memberId, MultipartFile imageFile, List<MultipartFile> stepImages) {
        try {
            // 1. 레시피 저장
            UUID recipeId =  saveRecipe(null, memberId, recipeRequestDto);

            // 2. 대표 이미지 업데이트
            saveMainImage(imageFile, memberId, recipeId);

            // 3. 재료 저장
            saveIngredienets(recipeRequestDto, recipeId);

            // 4. 조리순서 저장
            saveCookingOrders(recipeRequestDto, stepImages, recipeId);
            return recipeId;
        } catch (RecipeCreateException | IOException e) {
            throw new RecipeCreateException("레시피 저장 중 에러 발생");
        }


    }


    @Transactional
    public UUID updateRecipe(RecipeRequestDto recipeRequestDto, UUID memberId, MultipartFile imageFile, List<MultipartFile> stepImages, UUID recipeId) throws  IOException{
        try {
            // 1. 레시피 저장
            saveRecipe(recipeId, memberId, recipeRequestDto);

            // 2. 대표 이미지 업데이트
            saveMainImage(imageFile, memberId, recipeId);

            // 3. 재료 저장
            saveIngredienets(recipeRequestDto, recipeId);

            // 4. 조리순서 저장
            saveCookingOrders(recipeRequestDto, stepImages, recipeId);

            return recipeId;
        } catch ( RecipeCreateException e) {
            throw new RecipeCreateException("레시피 UPDATE 중 에러 발생");
        }


    }

    private UUID saveRecipe(UUID recipeId, UUID memberId, RecipeRequestDto recipeRequestDto) {
        Recipe recipe = Recipe.builder()
                .recipeId(recipeId)
                .memberId(memberId)
                .recipeTitle(recipeRequestDto.getRecipeTitle())
                .recipeContent(recipeRequestDto.getRecipeContent())
                .build();

       return recipeRepository.save(recipe).getRecipeId();

    }

    private UUID saveMainImage(MultipartFile imageFile, UUID memberId, UUID recipeId) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            return imageService.updateMainImage(imageFile, memberId, recipeId);
        } else {
            return null;
        }
    }

    private void saveIngredienets(RecipeRequestDto recipeRequestDto, UUID recipeId) {
        if (recipeRequestDto.getIngredients().size() > 0) {
            List<Ingredients> ingredients = recipeRequestDto.getIngredients().stream()
                    .map(ingredient -> {
                        return Ingredients.builder()
                                .ingredientsId(ingredient.getIngredientsId())
                                .recipeId(recipeId)
                                .ingredientsName(ingredient.getIngredientsName())
                                .build();
                    })
                    .collect(Collectors.toList());

            ingredientsRepository.saveAll(ingredients);
        }
    }

    private void saveCookingOrders(RecipeRequestDto recipeRequestDto, List<MultipartFile> stepImages, UUID recipeId) {
        List<CookingOrderDto> steps = recipeRequestDto.getCookingOrders();
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
    }

    @Transactional
    public void deleteRecipe(UUID recipeId) {
        // 존재 여부 확인 (Optional 처리 생략 가능)
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new NoSuchElementException("레시피가 존재하지 않습니다."));

        // 자식 엔티티 먼저 삭제
        cookingOrderRepository.deleteByRecipeId(recipeId);
        ingredientsRepository.deleteByRecipeId(recipeId);
        imageService.deleteByRecipeId(recipeId);

        // 마지막에 부모 엔티티 삭제
        recipeRepository.delete(recipe);
    }



}
