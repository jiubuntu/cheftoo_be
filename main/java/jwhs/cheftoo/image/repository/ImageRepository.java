package jwhs.cheftoo.image.repository;

import jwhs.cheftoo.image.entity.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;


public interface ImageRepository extends JpaRepository<Images, UUID> {

    Optional<Images> findFirstByRecipeId(UUID recipeId);

    @Modifying
    @Query("DELETE FROM Images i WHERE i.image_id :imageId")
    int deleteByImageId(@Param("imageId") UUID imageId);



}
