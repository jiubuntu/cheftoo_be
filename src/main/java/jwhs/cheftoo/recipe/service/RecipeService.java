package jwhs.cheftoo.recipe.service;

import jakarta.transaction.Transactional;
import jwhs.cheftoo.auth.entity.Member;
import jwhs.cheftoo.auth.service.MemberService;
import jwhs.cheftoo.cookingorder.service.CookingOrderService;
import jwhs.cheftoo.cookingorder.dto.CookingOrderRequestSaveDto;
import jwhs.cheftoo.cookingorder.entity.CookingOrder;
import jwhs.cheftoo.image.entity.Images;
import jwhs.cheftoo.image.enums.S3ImageType;
import jwhs.cheftoo.image.repository.ImageRepository;
import jwhs.cheftoo.image.service.ImageService;
import jwhs.cheftoo.image.service.S3Service;
import jwhs.cheftoo.recipe.dto.RecipeDetailResponseDto;
import jwhs.cheftoo.recipe.dto.RecipeRequestDto;
import jwhs.cheftoo.ingredient.entity.Ingredients;
import jwhs.cheftoo.recipe.entity.Recipe;
import jwhs.cheftoo.recipe.dto.RecipeResponseDto;
import jwhs.cheftoo.recipe.exception.RecipeCreateException;
import jwhs.cheftoo.cookingorder.repository.CookingOrderRepository;
import jwhs.cheftoo.ingredient.repository.IngredientsRepository;
import jwhs.cheftoo.recipe.repository.RecipeRepository;
import jwhs.cheftoo.sauce.entity.Sauce;
import jwhs.cheftoo.sauce.service.SauceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;



@Service
@Slf4j
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientsRepository ingredientsRepository;
    private final CookingOrderRepository cookingOrderRepository;
    private final ImageService imageService;
    private final MemberService memberService;
    private final CookingOrderService cookingOrderService;
    private final ImageRepository imageRepository;
    private final S3Service s3Service;
    private final RecipeViewService recipeViewService;
    private final SauceService sauceService;

    // 단건조회(상세조회)
    public RecipeDetailResponseDto findRecipeByRecipeId(UUID recipeId) {

        // 레시피 조회
        Recipe recipe = recipeRepository.findByRecipeId(recipeId)
                .orElseThrow(() -> new NoSuchElementException("해당되는 레시피를 찾을 수 없습니다."));

        // 대표 이미지 조회 (presignedURL 생성)
        RecipeDetailResponseDto.Images images = RecipeDetailResponseDto.Images.fromEntity(imageService.findMainImageByRecipe(recipe));
        String key = images.getImgPath();
        URL RecipeImagePrisignedGetUrl = s3Service.generateRecipeImagePresignedGetUrl(key, S3ImageType.RECIPE_GET_DURATION);
        String RecipeImageUrl = RecipeImagePrisignedGetUrl != null ? RecipeImagePrisignedGetUrl.toString() : null;
        images.setImgPath(RecipeImageUrl);


        // 재료 조회
        List<Ingredients> ingredientsList = ingredientsRepository.findAllByRecipe(recipe);
        List<RecipeDetailResponseDto.Ingredients> ingredients = ingredientsList.stream()
                .map(RecipeDetailResponseDto.Ingredients::fromEntity)
                .collect(Collectors.toList());

        // 소스 조회
        List<Sauce> sauceList = sauceService.findAllSauceByRecipe(recipe);
        List<RecipeDetailResponseDto.Sauce> sauce = sauceList.stream()
                .map(RecipeDetailResponseDto.Sauce::fromEntity)
                .collect(Collectors.toList());


        // 조리순서 조회 presignedURL 생성)
        List<CookingOrder> cookingOrderList = cookingOrderRepository.findByRecipeOrderByOrderDesc(recipe).stream()
                .map(step -> {
                    String objkey = step.getImgPath();
                    URL prisignedGetUrl = s3Service.generateCookingOrderImagePresignedGetUrl(objkey, S3ImageType.COOKING_ORDER_GET_DURATION);
                    String imageUrl = prisignedGetUrl != null ? prisignedGetUrl.toString() : null;
                    step.setImgPath(imageUrl);
                    return step;
                })
                .toList();
        List<RecipeDetailResponseDto.CookingOrder> cookingOrder = RecipeDetailResponseDto.CookingOrder.fromEntity(cookingOrderList);

        // 조회수 증가
        recipeViewService.incrementRecipeView(recipeId);

        return RecipeDetailResponseDto.builder()
                .recipeId(recipe.getRecipeId())
                .memberId(recipe.getMember().getMemberId())
                .recipeTitle(recipe.getRecipeTitle())
                .recipeContent(recipe.getRecipeContent())
                .images(images)
                .ingredients(ingredients)
                .sauce(sauce)
                .cookingOrder(cookingOrder)
                .build();

    }


    // 특정 멤버의 레시피 조회
    public List<RecipeResponseDto> findAllRecipesByMember(UUID memberId) {

        Member member = memberService.findMemberById(memberId);
        return  recipeRepository.findAllByMember(member).stream()
                .map(recipe -> {
                    String imgPath = imageService.findMainImageByRecipe(recipe).getImgPath();
                    return RecipeResponseDto.fromEntity(recipe, imgPath);
                })
                .collect(Collectors.toList());
    }


    // 전체조회
    public Page<RecipeResponseDto> findAllRecipes(Pageable pageable, String keyword) {
        return recipeRepository.findAllWithImage(pageable, keyword);
    }


    @Transactional
    public Recipe createRecipe(RecipeRequestDto recipeRequestDto, UUID memberId) {
        try {
            Member member = memberService.findMemberById(memberId);

            // 1. 레시피 메타데이터 저장
            Recipe recipe =  saveRecipe(null, member, recipeRequestDto);

            // 2. 대표 이미지 메타데이터 저장
            saveRecipeImage(member, recipe, recipeRequestDto);

            // 3. 재료 저장
            saveIngredienets(recipeRequestDto, recipe);

            // 4. 소스 저장
            saveSauce(recipeRequestDto, recipe);

            // 5. 조리순서 이미지 메타데이터 저장
            saveCookingOrders(recipeRequestDto, recipe, member);

            return recipe;
        } catch ( Exception e) {
            e.printStackTrace();
            throw new RecipeCreateException("레시피 저장 중 에러가 발생했습니다.");
        }
    }


    @Transactional
    public UUID updateRecipe(RecipeRequestDto recipeRequestDto, UUID memberId, MultipartFile imageFile, List<MultipartFile> stepImages, UUID recipeId) throws  IOException{
        try {
            Member member = memberService.findMemberById(memberId);
            // 1. 레시피 저장
            Recipe recipe = saveRecipe(recipeId, member, recipeRequestDto);

            // 2. 대표 이미지 업데이트
            saveRecipeImage(member, recipe, recipeRequestDto);

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


    private UUID saveRecipeImage(Member member, Recipe recipe, RecipeRequestDto recipeRequestDto) {
        return imageRepository.save(
                Images.builder()
                        .imageId(null)
                        .recipe(recipe)
                        .member(member)
                        .imgPath(recipeRequestDto.getRecipeImageKey())
                        .contentType(recipeRequestDto.getRecipeImageContentType())
                        .build()
        ).getImageId();

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

    private void saveSauce(RecipeRequestDto recipeRequestDto, Recipe recipe) {
        if (recipeRequestDto.getSauce().size() > 0) {
            List<Sauce> sauceList = recipeRequestDto.getSauce().stream()
                    .map(sauce -> {
                        return Sauce.builder()
                                .recipe(recipe)
                                .sauceName(sauce.getSauceName())
                                .quantity(sauce.getQuantity())
                                .build();
                    }).toList();


            sauceService.saveAll(sauceList);
        }
    }


    private void saveCookingOrders(RecipeRequestDto recipeRequestDto, Recipe recipe, Member member) throws IOException {
        List<CookingOrderRequestSaveDto> steps = recipeRequestDto.getCookingOrder();

        if (steps.isEmpty() && steps == null) return ;

        List<CookingOrder> cookingOrderForSave = steps.stream()
                .map( step -> {
                            return CookingOrder.builder()
                                    .recipe(recipe)
                                    .order(step.getOrder())
                                    .content(step.getContent())
                                    .imgPath(step.getCookingOrderImageKey())
                                    .build();
                        }

                ).toList();

        cookingOrderRepository.saveAll(cookingOrderForSave);
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
        Recipe recipe = recipeRepository.findByRecipeId(recipeId)
                .orElseThrow(() -> new NoSuchElementException("레시피가 존재하지 않습니다."));

        // 레시피 이미지 S3에서 삭제
        deleteRecipeImageByRecipe(recipe);
        // 조리순서 이미지 S3에서 삭제
        cookingOrderService.deleteCookingOrderImageByRecipe(recipe);

        // 자식 엔티티 먼저 삭제
        cookingOrderRepository.deleteByRecipe(recipe);
        ingredientsRepository.deleteByRecipe(recipe);
        imageService.deleteByRecipeId(recipe);
        sauceService.deleteByRecipe(recipe);

        // 헤더 삭제
        recipeRepository.delete(recipe);
    }



    public void deleteRecipeImageByRecipe(Recipe recipe) {
        String savedRecipeImageKey = imageService.findMainImageByRecipe(recipe).getImgPath();
        s3Service.deleteRecipeImage(savedRecipeImageKey);
    }



}
