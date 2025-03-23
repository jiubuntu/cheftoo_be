package jwhs.cheftoo.recipe.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jwhs.cheftoo.recipe.dto.RecipeRequestDto;
import jwhs.cheftoo.recipe.service.RecipeService;
import jwhs.cheftoo.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

//    @PostMapping("/")
//    public ResponseEntity<?> createRecipe(
//            @RequestBody RecipeRequestDto dto,
//            HttpServletRequest request
//    ) {
//        // JWT의 Payload에서 memberId 추출
//        String token = jwtUtil.getTokenFromRequest(request);
//        String memberId = jwtUtil.getMemberIdFromToken(token);
//
//        UUID recipeId = recipeService.createRecipe(dto, memberId);
//    }


}
