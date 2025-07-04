package jwhs.cheftoo.recipe.service;

import jakarta.transaction.Transactional;
import jwhs.cheftoo.auth.entity.Member;
import jwhs.cheftoo.auth.service.MemberService;
import jwhs.cheftoo.cookingorder.dto.CookingOrderRequestSaveDto;
import jwhs.cheftoo.image.service.ImageService;
import jwhs.cheftoo.recipe.dto.RecipeDetailResponseDto;
import jwhs.cheftoo.recipe.dto.RecipeRequestDto;
import jwhs.cheftoo.ingredient.entity.Ingredients;
import jwhs.cheftoo.recipe.entity.Recipe;
import jwhs.cheftoo.recipe.dto.RecipeResponseDto;
import jwhs.cheftoo.recipe.exception.RecipeCreateException;
import jwhs.cheftoo.cookingorder.repository.CookingOrderRepository;
import jwhs.cheftoo.ingredient.repository.IngredientsRepository;
import jwhs.cheftoo.recipe.repository.RecipeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    private MemberService memberService;

    public RecipeService(
            RecipeRepository recipeRepository,
            IngredientsRepository ingredientsRepository,
            CookingOrderRepository cookingOrderRepository,
            ImageService imageService,
            MemberService memberService
    ) {
        this.recipeRepository = recipeRepository;
        this.ingredientsRepository = ingredientsRepository;
        this.cookingOrderRepository = cookingOrderRepository;
        this.imageService = imageService;
        this.memberService = memberService;
    }


    // 단건조회(상세조회)
    @Transactional
    public RecipeDetailResponseDto findRecipeByRecipeId(UUID recipeId) {
        // 레시피 조회
        Recipe recipe = recipeRepository.findByRecipeId(recipeId)
                .orElseThrow(() -> new NoSuchElementException("해당되는 레시피를 찾을 수 없습니다."));
        // 대표 이미지 조회
        RecipeDetailResponseDto.Images images = RecipeDetailResponseDto.Images.fromEntity(imageService.findMainImageByRecipeId(recipe));

        // 재료 조회
        List<Ingredients> ingredientsList = ingredientsRepository.findAllByRecipe(recipe);
        List<RecipeDetailResponseDto.Ingredients> ingredients = ingredientsList.stream()
                .map(RecipeDetailResponseDto.Ingredients::fromEntity)
                .collect(Collectors.toList());


        // 조리순서 조회
        List<RecipeDetailResponseDto.CookingOrder> cookingOrder = RecipeDetailResponseDto.CookingOrder.fromEntity(cookingOrderRepository.findByRecipeOrderByOrderDesc(recipe));

        return RecipeDetailResponseDto.builder()
                .recipeId(recipe.getRecipeId())
                .memberId(recipe.getMember().getMemberId())
                .recipeTitle(recipe.getRecipeTitle())
                .recipeContent(recipe.getRecipeContent())
                .images(images)
                .ingredients(ingredients)
                .cookingOrder(cookingOrder)
                .build();
    }


    // 특정 멤버의 레시피 조회
    public List<RecipeResponseDto> findAllRecipesByMember(UUID memberId) {

        Member member = memberService.findMemberById(memberId);
        return  recipeRepository.findAllByMember(member).stream()
                .map(recipe -> {
                    String imgPath = imageService.findMainImageByRecipeId(recipe).getImgPath();
                    return RecipeResponseDto.fromEntity(recipe, imgPath);
                })
                .collect(Collectors.toList());
    }


    // 전체조회
    public Page<RecipeResponseDto> findAllRecipes(Pageable pageable) {
        return recipeRepository.findAllWithImage(pageable);
    }


    @Transactional
    public Recipe createRecipe(RecipeRequestDto recipeRequestDto, UUID memberId, MultipartFile imageFile, List<MultipartFile> stepImages) {
        try {
            Member member = memberService.findMemberById(memberId);
            // 1. 레시피 저장
            Recipe recipe =  saveRecipe(null, member, recipeRequestDto);

            // 2. 대표 이미지 저장
            saveMainImage(imageFile, member, recipe);

            // 3. 재료 저장
            saveIngredienets(recipeRequestDto, recipe);

            // 4. 조리순서 저장
            saveCookingOrders(recipeRequestDto, stepImages, recipe);

            return recipe;
        } catch ( Exception e) {
            throw new RecipeCreateException("레시피 저장 중 에러 발생");
        }
    }


    @Transactional
    public UUID updateRecipe(RecipeRequestDto recipeRequestDto, UUID memberId, MultipartFile imageFile, List<MultipartFile> stepImages, UUID recipeId) throws  IOException{
        try {
            Member member = memberService.findMemberById(memberId);
            // 1. 레시피 저장
            Recipe recipe = saveRecipe(recipeId, member, recipeRequestDto);

            // 2. 대표 이미지 업데이트
            saveMainImage(imageFile, member, recipe);

            // 3. 재료 저장
            saveIngredienets(recipeRequestDto, recipe);

//            // 4. 조리순서 저장
//            updateCookingOrders(recipeRequestDto, stepImages, recipeId);

            return recipeId;
        } catch ( RecipeCreateException e) {
            throw new RecipeCreateException("레시피 UPDATE 중 에러 발생");
        }


    }

    private Recipe saveRecipe(UUID recipeId, Member member, RecipeRequestDto recipeRequestDto) {
        Recipe recipe = Recipe.builder()
                .recipeId(recipeId)
                .member(member)
                .recipeTitle(recipeRequestDto.getRecipeTitle())
                .recipeContent(recipeRequestDto.getRecipeContent())
                .build();

       return recipeRepository.save(recipe);

    }

    private UUID saveMainImage(MultipartFile imageFile, Member member, Recipe recipe) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
//            return imageService.updateMainImage(imageFile, memberId, recipeId);
            return imageService.saveMainImageMetaAndFile(imageFile, member, recipe);
        } else {
            return null;
        }
    }

//    private UUID updateMainImage(MultipartFile imageFile, Member member, Recipe recipe) {
        // 파일시스템에 저장된 메인이미지와 다른지 비교

        // 다르면 저장
        // 같으면 pass
//    }

    private void saveIngredienets(RecipeRequestDto recipeRequestDto, Recipe recipe) {
        if (recipeRequestDto.getIngredients().size() > 0) {
            List<Ingredients> ingredients = recipeRequestDto.getIngredients().stream()
                    .map(ingredient -> {
                        return Ingredients.builder()
                                .recipe(recipe)
                                .ingredientsName(ingredient.getIngredientsName())
                                .ingredientsNum(ingredient.getIngredientsNum())
                                .build();
                    })
                    .collect(Collectors.toList());

            ingredientsRepository.saveAll(ingredients);
        }
    }


    private void saveCookingOrders(RecipeRequestDto recipeRequestDto, List<MultipartFile> stepImages, Recipe recipe) {
        List<CookingOrderRequestSaveDto> steps = recipeRequestDto.getCookingOrders();

        if (steps.isEmpty() && steps == null) return ;

        int idx = 1;

        for (int i = 0; i < steps.size(); i++) {
            MultipartFile stepImage =  stepImages.get(i);
            CookingOrderRequestSaveDto step = steps.get(i);
            imageService.saveCookingOrderImageMetaAndFile(step, stepImage, recipe, idx);
            idx ++;
        }

        imageService.registerCookingOrderImageFileSaveTask();

    }

//    private void updateCookingOrders(RecipeRequestDto recipeRequestDto, List<MultipartFile> stepImages, UUID recipeId) {
//        List<CookingOrderDto> steps = recipeRequestDto.getCookingOrders();
//        if (!steps.isEmpty() && steps != null ) {
//            int idx = 1;
//            for (int i = 0; i < steps.size(); i++) {
//                String content = steps.get(i).getContent();
//                MultipartFile image = stepImages.get(i);
//                String imagePath = imageService.saveCookingOrderImage(image);
//
//                // 저장 로직: content + image 조합
//                cookingOrderRepository.save(
//                        CookingOrder.builder()
//                                .recipeId(recipeId)
//                                .order(idx)
//                                .content(content)
//                                .imgPath(imagePath)
//                                .build()
//                );
//
//                idx ++;
//            }
//        }
//    }

    @Transactional
    public void deleteRecipe(UUID recipeId) {
        // 존재 여부 확인 (Optional 처리 생략 가능)
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new NoSuchElementException("레시피가 존재하지 않습니다."));

        // 자식 엔티티 먼저 삭제
        cookingOrderRepository.deleteByRecipe(recipe);
        ingredientsRepository.deleteByRecipe(recipe);
        imageService.deleteByRecipeId(recipe);

        // 마지막에 부모 엔티티 삭제
        recipeRepository.delete(recipe);
    }



}
