package jwhs.cheftoo.repository;
import jwhs.cheftoo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByKakaoId(String kakaoId);
}
