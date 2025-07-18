package jwhs.cheftoo.sauce.repository;

import jwhs.cheftoo.recipe.entity.Recipe;
import jwhs.cheftoo.sauce.entity.Sauce;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SauceRepository extends JpaRepository<Sauce, UUID> {

    List<Sauce> findAllByRecipe(Recipe recipe);
    void deleteByRecipe(Recipe recipe);
}
