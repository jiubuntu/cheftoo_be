package jwhs.cheftoo.scrap.repository;

import jwhs.cheftoo.auth.entity.Member;
import jwhs.cheftoo.scrap.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ScrapRepository extends JpaRepository<Scrap, UUID> {
    List<Scrap> findAllByMember(Member member);
    void deleteAllByMember(Member member);
}
