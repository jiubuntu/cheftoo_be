package jwhs.cheftoo.recipe.repository;

import jwhs.cheftoo.recipe.entity.Ingredients;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IngredientsRepository extends JpaRepository<Ingredients, UUID> {
}
