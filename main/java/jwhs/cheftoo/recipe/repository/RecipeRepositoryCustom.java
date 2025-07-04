package jwhs.cheftoo.recipe.repository;

import jwhs.cheftoo.recipe.dto.RecipeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecipeRepositoryCustom {
    Page<RecipeResponseDto> findAllWithImage(Pageable pageable);
}
