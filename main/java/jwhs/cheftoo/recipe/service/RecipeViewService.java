package jwhs.cheftoo.recipe.service;


import jwhs.cheftoo.recipe.dto.RecipeResponseDto;
import jwhs.cheftoo.recipe.exception.RecipeFindException;
import jwhs.cheftoo.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
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
    public List<RecipeResponseDto> getRecipeByViewsOrder() {
        try {
            Set<String> recipeIds = redisTemplate.opsForZSet()
                    .reverseRange("recipe_views", 0, 9);

            if (recipeIds.isEmpty()) {
                // ZSET이 비어있을경우 최신순으로 조회
                return recipeRepository.findRecipesDateOrder();
            }

            List<UUID> recipeIdList = recipeIds.stream()
                    .map(UUID::fromString)
                    .collect(Collectors.toList());

            return recipeRepository.findRecipesByViewsOrder(recipeIdList);
        } catch (RedisConnectionFailureException e) {
            log.error("[RecipeViewService - getAllRecipeByViewsOrder] redis 연결 실패");
            // fallback -> 최신순으로 조회
            return recipeRepository.findRecipesDateOrder();
        } catch ( Exception e) {
            e.printStackTrace();
            log.error("[RecipeViewService - getAllRecipeByViewsOrder] 인기 레시피 조회 중 에러 발생");
            throw new RecipeFindException("레시피 조회 중 에러가 발생했슴니다.");
        }

    }


}
