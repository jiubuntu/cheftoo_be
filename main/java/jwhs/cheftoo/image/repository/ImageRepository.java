package jwhs.cheftoo.image.repository;

import jwhs.cheftoo.image.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


public interface ImageRepository extends JpaRepository<Images, UUID> {


}
