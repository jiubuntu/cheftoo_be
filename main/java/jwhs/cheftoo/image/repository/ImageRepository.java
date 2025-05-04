package jwhs.cheftoo.image.repository;

import jakarta.transaction.Transactional;
import jwhs.cheftoo.image.entity.Images;
import jwhs.cheftoo.recipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;


public interface ImageRepository extends JpaRepository<Images, UUID> {

    Optional<Images> findMainImageByRecipe(Recipe recipe);

    @Modifying
    @Query("DELETE FROM Images i WHERE i.imageId = :imageId")
    int deleteByImageId(@Param("imageId") UUID imageId);


    void deleteByRecipe(Recipe recipe);


}
