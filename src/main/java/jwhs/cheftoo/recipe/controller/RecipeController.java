package jwhs.cheftoo.recipe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jwhs.cheftoo.recipe.dto.RecipeRequestDto;
import jwhs.cheftoo.recipe.dto.RecipeResponseDto;
import jwhs.cheftoo.recipe.service.RecipeService;
import jwhs.cheftoo.recipe.service.RecipeViewService;
import jwhs.cheftoo.util.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


//레시피
@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    private RecipeService recipeService;
    private RecipeViewService recipeViewService;
    private JwtUtil jwtUtil;

    public RecipeController(
            RecipeService recipeService,
            JwtUtil jwtUtil,
            RecipeViewService recipeViewService
    ) {
        this.recipeService = recipeService;
        this.jwtUtil = jwtUtil;
        this.recipeViewService = recipeViewService;
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<?> getRecipe(
            @PathVariable("recipeId") UUID recipeId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(recipeService.findRecipeByRecipeId(recipeId));
    }


    @GetMapping("/member")
    public ResponseEntity<?> getAllRecipeByMember(
            HttpServletRequest request,
            @PageableDefault(size = 12, sort = "dataCreated", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        String token = jwtUtil.getAccessTokenFromRequest(request);
        UUID memberId = jwtUtil.getMemberIdFromToken(token);

        return ResponseEntity.status(HttpStatus.OK).body(recipeService.findAllRecipesByMember(memberId, pageable)) ;
    }

    @GetMapping
    public ResponseEntity<Page<RecipeResponseDto>> getAllRecipe(
            @PageableDefault(size = 12, sort = "dataCreated", direction = Sort.Direction.DESC)
            Pageable pageable,
            @RequestParam(value = "keyword", required = false) String keyword
            )
    {
        return ResponseEntity.status(HttpStatus.OK).body(recipeService.findAllRecipes(pageable, keyword));
    }

    @GetMapping("/popular-top10")
    public ResponseEntity<List<RecipeResponseDto>> getRecipePopularTop10(

    ) {
        return ResponseEntity.ok(recipeViewService.getRecipeByViewsOrder());
    }

    @PostMapping
    public ResponseEntity<?> createRecipe(
            @RequestBody RecipeRequestDto dto,
            HttpServletRequest request
    ) {

        return handleRecipeSave(dto, request, null);
    }


    @PutMapping("/{recipeId}")
    public ResponseEntity<?> updateRecipe(
            @RequestPart("data") RecipeRequestDto dto,
            @RequestPart("image") MultipartFile imageFile, // 대표 이미지
            @RequestPart("cookingStepImages") List<MultipartFile> stepImages, // 조리순서 이미지
            HttpServletRequest request,
            @PathVariable("recipeId") UUID recipeId
    ) {
        return handleRecipeSave(dto, request, recipeId);
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<?> deleteRecipe(
            @PathVariable("recipeId") UUID recipeId
    ) {
        try {
            recipeService.deleteRecipe(recipeId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.noContent().build();

    }


    private ResponseEntity<?> handleRecipeSave(
            RecipeRequestDto dto,
            HttpServletRequest request,
            UUID recipeId

    ) {
        // JWT의 Payload에서 memberId 추출
        String token = jwtUtil.getAccessTokenFromRequest(request);
        UUID memberId = jwtUtil.getMemberIdFromToken(token);
        UUID savedRecipeId = null;

        if (recipeId == null) { // INSESRT
            savedRecipeId = recipeService.createRecipe(dto, memberId).getRecipeId();
        } else { // UPDATE
            try {
//                savedRecipeId = recipeService.updateRecipe(dto, memberId, imageFile, stepImages, recipeId);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return ResponseEntity.status(HttpStatus.OK).body(savedRecipeId);
    }


}
