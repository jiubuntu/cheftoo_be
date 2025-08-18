package jwhs.cheftoo.scrap.controller;

import jakarta.servlet.http.HttpServletRequest;
import jwhs.cheftoo.scrap.dto.ScrapInRecipeDeleteRequestDto;
import jwhs.cheftoo.scrap.dto.ScrapInRecipeResponseDto;
import jwhs.cheftoo.scrap.service.ScrapInRecipeService;
import jwhs.cheftoo.util.JwtUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ScrapInRecipeController {
    private ScrapInRecipeService scrapInRecipeService;
    private JwtUtil jwtUtil;


    public ScrapInRecipeController(ScrapInRecipeService scrapInRecipeService, JwtUtil jwtUtil) {
        this.scrapInRecipeService = scrapInRecipeService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("member/scrap/{scrapId}/recipe/{recipeId}")
    public ResponseEntity<ScrapInRecipeResponseDto> saveScrapInRecipe(
           @PathVariable("scrapId") UUID scrapId,
           @PathVariable("recipeId") UUID recipeId,
           HttpServletRequest request
    ) {

        scrapInRecipeService.saveScrapInRecipe(scrapId, recipeId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @GetMapping("member/scrap/{scrapId}/recipe")
    public ResponseEntity<ScrapInRecipeResponseDto> getAllRecipeByScrapId(
            @PathVariable("scrapId") UUID scrapId,
            @PageableDefault(size = 12, sort = "dataCreated", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        ScrapInRecipeResponseDto resultDto = scrapInRecipeService.findRecipeListByScrapId(scrapId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }


    @DeleteMapping("member/scrap/recipe")
    public ResponseEntity<?> deleteScrapInRecipe(
            @RequestBody ScrapInRecipeDeleteRequestDto dto
            ) {
        scrapInRecipeService.deleteScrapInRecipeByScrapIdAndRecipeId(dto);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
