package jwhs.cheftoo.recipe.repository;

import jwhs.cheftoo.recipe.entity.CookingOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CookingOrderRepository extends JpaRepository<CookingOrder, UUID> {

    List<CookingOrder> findByRecipeIdOrderByOrderDesc(UUID recipeId); // order(순서)컬럼은 내림차순으로 가져오기
}
