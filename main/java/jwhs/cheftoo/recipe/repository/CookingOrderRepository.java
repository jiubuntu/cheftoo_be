package jwhs.cheftoo.recipe.repository;

import jwhs.cheftoo.recipe.entity.CookingOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CookingOrderRepository extends JpaRepository<CookingOrder, UUID> {
}
