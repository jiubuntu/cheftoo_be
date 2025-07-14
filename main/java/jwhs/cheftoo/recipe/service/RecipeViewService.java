package jwhs.cheftoo.recipe.service;


import jwhs.cheftoo.recipe.dto.RecipeResponseDto;
import jwhs.cheftoo.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeViewService {

    private final RedisTemplate<String, String > redisTemplate;
    private final String ZSET_KEY = "recipe_views";
    private final RecipeRepository recipeRepository;

    public void incrementRecipeView(UUID recipeId) {
        String Key = recipeId.toString();
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();

        // 현재 점수 조회
        Double currentScore = zSetOps.score(ZSET_KEY, Key);

        if (currentScore == null ) {
            // ZSET에 없으면 추가
            zSetOps.add(ZSET_KEY, Key, 1.0);
        } else {
            zSetOps.incrementScore(ZSET_KEY, Key, 1.0);
        }
    }

    // 조회수 순으로 레시피 조회 (10개)
    public List<RecipeResponseDto> getAllRecipeByViewsOrder() {
        Set<String> recipeIds = redisTemplate.opsForZSet()
                .reverseRange("recipe_views", 0, 9);

        List<UUID> recipeIdList = recipeIds.stream()
                .map(UUID::fromString)
                .collect(Collectors.toList());

        return recipeRepository.findRecipesByViewsOrder(recipeIdList);

    }


}
