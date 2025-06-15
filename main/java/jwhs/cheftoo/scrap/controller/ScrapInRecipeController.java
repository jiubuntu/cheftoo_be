package jwhs.cheftoo.scrap.controller;

import jakarta.servlet.http.HttpServletRequest;
import jwhs.cheftoo.scrap.dto.ScrapInRecipeResponseDto;
import jwhs.cheftoo.scrap.service.ScrapInRecipeService;
import jwhs.cheftoo.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ScrapInRecipeController {
    private ScrapInRecipeService scrapInRecipeService;
    private JwtUtil jwtUtil;


    public ScrapInRecipeController(ScrapInRecipeService scrapInRecipeService, JwtUtil jwtUtil) {
        this.scrapInRecipeService = scrapInRecipeService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("member/scrap/recipe")
    public ResponseEntity<ScrapInRecipeResponseDto> saveScrapInRecipe(
           @PathVariable("scrapId") UUID scrapId,
           @PathVariable("recipeId") UUID recipeId,
           HttpServletRequest request
    ) {

        scrapInRecipeService.saveScrapInRecipe(scrapId, recipeId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @GetMapping("member/scrap/recipe")
    public ResponseEntity<ScrapInRecipeResponseDto> getAllRecipeByScrapId(
            @PathVariable("scrapId") UUID scrapId
    ) {
        ScrapInRecipeResponseDto resultDto = scrapInRecipeService.findRecipeListByScrapId(scrapId);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }

}
