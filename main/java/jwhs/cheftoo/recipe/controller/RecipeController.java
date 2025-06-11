package jwhs.cheftoo.recipe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jwhs.cheftoo.image.service.ImageService;
import jwhs.cheftoo.recipe.dto.RecipeRequestDto;
import jwhs.cheftoo.recipe.entity.Recipe;
import jwhs.cheftoo.recipe.service.RecipeService;
import jwhs.cheftoo.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


//레시피
@RestController
@RequestMapping("/recipe")
public class RecipeController {

    private RecipeService recipeService;
    private JwtUtil jwtUtil;

    public RecipeController(RecipeService recipeService, JwtUtil jwtUtil) {
        this.recipeService = recipeService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<?> getRecipe(@PathVariable("recipeId") UUID recipeId) {
        return ResponseEntity.status(HttpStatus.OK).body(recipeService.findRecipeByRecipeId(recipeId));
    }

    @GetMapping("/member")
    public ResponseEntity<?> getAllRecipeByMember( HttpServletRequest request) {
        String token = jwtUtil.getTokenFromRequest(request);
        UUID memberId = jwtUtil.getMemberIdFromToken(token);

        return ResponseEntity.status(HttpStatus.OK).body(recipeService.findAllRecipesByMember(memberId)) ;
    }

    @GetMapping
    public ResponseEntity<?> getAllRecipe() {
        return ResponseEntity.status(HttpStatus.OK).body(recipeService.findAllRecipes());
    }

    @PostMapping
    public ResponseEntity<?> createRecipe(
            @RequestPart("data") MultipartFile jsonFile,
            @RequestPart("image") MultipartFile imageFile, // 대표 이미지
            @RequestPart("cookingStepImages") List<MultipartFile> stepImages, // 조리순서 이미지
            HttpServletRequest request
    ) {
        RecipeRequestDto dto;
        try {
            ObjectMapper mapper = new ObjectMapper();
            dto = mapper.readValue(jsonFile.getInputStream(), RecipeRequestDto.class);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("JSON 파싱 실패");
        }
        return handleRecipeSave(dto, imageFile, stepImages, request, null);
    }


    @PutMapping("/{recipeId}")
    public ResponseEntity<?> updateRecipe(
            @RequestPart("data") RecipeRequestDto dto,
            @RequestPart("image") MultipartFile imageFile, // 대표 이미지
            @RequestPart("cookingStepImages") List<MultipartFile> stepImages, // 조리순서 이미지
            HttpServletRequest request,
            @PathVariable("recipeId") UUID recipeId
    ) {
        return handleRecipeSave(dto, imageFile, stepImages, request, recipeId);
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
            MultipartFile imageFile,
            List<MultipartFile> stepImages,
            HttpServletRequest request,
            UUID recipeId

    ) {
        // JWT의 Payload에서 memberId 추출
        String token = jwtUtil.getTokenFromRequest(request);
        UUID memberId = jwtUtil.getMemberIdFromToken(token);
        UUID savedRecipeId = null;

        if (recipeId == null) { // INSESRT
            savedRecipeId = recipeService.createRecipe(dto, memberId, imageFile, stepImages).getRecipeId();
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
