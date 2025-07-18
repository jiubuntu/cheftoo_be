package jwhs.cheftoo.auth.repository;
import jwhs.cheftoo.auth.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByKakaoId(String kakaoId);

    @Query("SELECT m.nickname FROM Member m WHERE memberId = :memberId")
    String findNickNameByMemberId(@Param("memberId") UUID memberId);


}
