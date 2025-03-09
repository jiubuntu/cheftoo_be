package jwhs.cheftoo.repository;
import jwhs.cheftoo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByMemberId(Long memberId);

    Optional<Member> findByKakaoId(String kakaoId);

    Optional<Member> findByName(String name);



}
