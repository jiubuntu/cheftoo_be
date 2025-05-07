package jwhs.cheftoo.cookingorder.repository;

import jwhs.cheftoo.cookingorder.entity.CookingOrder;
import jwhs.cheftoo.recipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CookingOrderRepository extends JpaRepository<CookingOrder, UUID> {

    List<CookingOrder> findByRecipeOrderByOrderDesc(Recipe recipe); // order(순서)컬럼은 내림차순으로 가져오기
    void deleteByRecipe(Recipe recipe);
}
