package jwhs.cheftoo.scrap.repository;

import jwhs.cheftoo.scrap.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ScrapRepository extends JpaRepository<Scrap, UUID> {
}
