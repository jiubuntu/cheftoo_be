//package jwhs.cheftoo.recipe;
//
//
//import jwhs.cheftoo.cookingorder.dto.CookingOrderDto;
//import jwhs.cheftoo.cookingorder.entity.CookingOrder;
//import jwhs.cheftoo.cookingorder.repository.CookingOrderRepository;
//import jwhs.cheftoo.image.entity.Images;
//import jwhs.cheftoo.image.service.ImageService;
//import jwhs.cheftoo.ingredient.entity.Ingredients;
//import jwhs.cheftoo.ingredient.repository.IngredientsRepository;
//import jwhs.cheftoo.recipe.dto.RecipeDetailResponseDto;
//import jwhs.cheftoo.ingredient.dto.IngredientsDto;
//import jwhs.cheftoo.recipe.dto.RecipeRequestDto;
//import jwhs.cheftoo.recipe.dto.RecipeResponseDto;
//import jwhs.cheftoo.recipe.entity.Recipe;
//import jwhs.cheftoo.recipe.repository.RecipeRepository;
//import jwhs.cheftoo.recipe.service.RecipeService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.web.multipart.MultipartFile;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class RecipeServiceTest {
//    @Mock private RecipeRepository recipeRepository;
//    @Mock private IngredientsRepository ingredientsRepository;
//    @Mock private CookingOrderRepository cookingOrderRepository;
//    @Mock private ImageService imageService;
//
//    @InjectMocks RecipeService recipeService;
//
//    @Test
//    void 레시피조회_정상조회_단건() {
//        UUID recipeId = UUID.randomUUID();
//        UUID memberId = UUID.randomUUID();
//
//        //given
//        Recipe recipe = Recipe.builder()
//                .recipeId(recipeId)
//                .member(memberId)
//                .recipeTitle("test recipe title")
//                .recipeContent("test recipe content")
//                .build();
//
//        Images images = Images.builder()
//                        .imageId(UUID.randomUUID())
//                        .recipeId(recipeId)
//                        .memberId(memberId)
//                        .imgPath("img.jpg")
//                        .build();
//
//
//        Ingredients ingredients = Ingredients.builder()
//                        .ingredientsId(UUID.randomUUID())
//                        .recipeId(recipeId)
//                        .ingredientsName("test ingredients name")
//                        .ingredientsNum("1개")
//                        .build();
//
//
//        CookingOrder step1 = CookingOrder.builder()
//                .cookingOrderId(UUID.randomUUID())
//                .recipeId(recipeId)
//                .order(1)
//                .content("test cookingorder test")
//                .imgPath("cookingorder.jpg")
//                .build();
//
//
//
//        when(recipeRepository.findByRecipeId(recipeId)).thenReturn(Optional.of(recipe));
//        when(imageService.findMainImageByRecipeId(recipeId)).thenReturn(images);
//        when(ingredientsRepository.findByRecipeId(recipeId)).thenReturn(Optional.of(ingredients));
//        when(cookingOrderRepository.findByRecipeIdOrderByOrderDesc(recipeId)).thenReturn(List.of(step1));
//
//        //when
//        RecipeDetailResponseDto result = recipeService.findRecipeByRecipeId(recipeId);
//
//
//        //then
//        assertEquals(recipeId, result.getRecipeId());
//        assertEquals(memberId, result.getMemberId());
//        assertEquals("test recipe title", result.getRecipeTitle());
//        assertEquals("test recipe content", result.getRecipeContent());
//        assertEquals("img.jpg", result.getImages().getImgPath());
//        assertEquals("test ingredients name", result.getIngredients().getIngredientsName());
//        assertEquals("1개", result.getIngredients().getIngredientsNum());
//        assertEquals(1, result.getCookingOrder().get(0).getOrder());
//        assertEquals("test cookingorder test", result.getCookingOrder().get(0).getContent());
//        assertEquals("cookingorder.jpg", result.getCookingOrder().get(0).getImgPath());
//
//
//    }
//
//    @Test
//    void 레시피조회_정상조회_다건() {
//        //given
//        UUID recipeId = UUID.randomUUID();
//        UUID memberId = UUID.randomUUID();
//
//        UUID recipeId1 = UUID.randomUUID();
//        UUID memberId1 = UUID.randomUUID();
//
//        Recipe recipe1 = Recipe.builder()
//                        .recipeId(recipeId)
//                        .memberId(memberId)
//                        .recipeTitle("test recipe1 title")
//                        .recipeContent("test recipe1 content")
//                        .build();
//
//        Recipe recipe2 = Recipe.builder()
//                .recipeId(recipeId1)
//                .memberId(memberId1)
//                .recipeTitle("test recipe2 title")
//                .recipeContent("test recipe2 content")
//                .build();
//
//        List<Recipe> recipeList = List.of(recipe1, recipe2);
//
//        when(recipeRepository.findAll()).thenReturn(recipeList);
//
//        //when
//        List<RecipeResponseDto> result = recipeService.findAllRecipes();
//
//        //then
//        assertEquals(2, result.size());
//
//        assertEquals(recipeId, result.get(0).getRecipeId());
//        assertEquals(memberId, result.get(0).getMemberId());
//        assertEquals("test recipe1 title", result.get(0).getRecipeTitle());
//        assertEquals("test recipe1 content", result.get(0).getRecipeContent());
//
//        assertEquals(recipeId1, result.get(1).getRecipeId());
//        assertEquals(memberId1, result.get(1).getMemberId());
//        assertEquals("test recipe2 title", result.get(1).getRecipeTitle());
//        assertEquals("test recipe2 content", result.get(1).getRecipeContent());
//
//    }
//
//    @Test
//    void 레시피생성_성공() throws IOException {
//        //given
//        UUID recipeId = UUID.randomUUID();
//        UUID memberId = UUID.randomUUID();
//        UUID mainImageId = UUID.randomUUID();
//
//        RecipeRequestDto recipeRequestDto = RecipeRequestDto.builder()
//                .recipeTitle("test recipe title")
//                .recipeContent("test recipe content")
//                .ingredients(List.of(IngredientsDto.builder()
//                                .ingredientsId(UUID.randomUUID())
//                                .recipeId(recipeId)
//                                .ingredientsName("test ingredients name")
//                                .ingredientsNum("test ingredients num")
//                                .build()))
//                .cookingOrders(List.of(CookingOrderDto.builder()
//                                .cookingOrderId(UUID.randomUUID())
//                                .recipeId(recipeId)
//                                .order(1)
//                                .content("test cookingOrders content")
//                                .imgPath("step1.jpg")
//                                .build()))
//                .build();
//
//        MultipartFile mainImage = mock(MultipartFile.class);
//        List<MultipartFile> step1Image = List.of(mock(MultipartFile.class));
//
//
//        when(recipeRepository.save(any(Recipe.class))).thenAnswer(invocation -> {
//            Recipe recipe = invocation.getArgument(0); // save 함수 첫번째 인자 가져옴
//            recipe.setRecipeId(recipeId);
//            return recipe;
//        });
//
//        when(imageService.updateMainImage(mainImage, memberId, recipeId)).thenReturn(mainImageId); // 대표이미지
//        when(imageService.saveCookingOrderImage(step1Image.get(0))).thenReturn("step1.jpg");
//
//        // when
//        UUID resultId = recipeService.createRecipe(recipeRequestDto, memberId, mainImage, step1Image);
//
//        //then
//        assertEquals(recipeId, resultId);
//        verify(recipeRepository).save(any(Recipe.class));
//        verify(imageService).updateMainImage(mainImage, memberId, recipeId);
//
//        verify(imageService).saveCookingOrderImage(step1Image.get(0));
//        verify(ingredientsRepository).saveAll(anyList());
//        verify(cookingOrderRepository).save(any(CookingOrder.class));
//
//
//
//    }
//
//
//
//}
