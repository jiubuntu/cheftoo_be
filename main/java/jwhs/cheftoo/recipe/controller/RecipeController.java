package jwhs.cheftoo.recipe.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jwhs.cheftoo.image.service.ImageService;
import jwhs.cheftoo.recipe.dto.RecipeRequestDto;
import jwhs.cheftoo.recipe.service.RecipeService;
import jwhs.cheftoo.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.List;
import java.util.UUID;


//레시피
@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private RecipeService recipeService;
    private JwtUtil jwtUtil;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/")
    public ResponseEntity<?> createRecipe(
//            @RequestBody RecipeRequestDto dto,
            @RequestPart("data") RecipeRequestDto dto,
            @RequestPart("image") MultipartFile imageFile, // 대표 이미지
            @RequestPart("cookingStepImages") List<MultipartFile> stepImages, // 조리순서 이미지
            HttpServletRequest request
    ) {

        // JWT의 Payload에서 memberId 추출
        String token = jwtUtil.getTokenFromRequest(request);
        String memberId = jwtUtil.getMemberIdFromToken(token);

        // 레시피 저장
        UUID recipeId = recipeService.createRecipe(dto, memberId, imageFile, stepImages);

        return ResponseEntity.status(HttpStatus.CREATED).body(recipeId);


    }


}
