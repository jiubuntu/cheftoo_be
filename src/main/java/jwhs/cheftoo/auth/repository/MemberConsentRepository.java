package jwhs.cheftoo.auth.repository;

import jakarta.transaction.Transactional;
import jwhs.cheftoo.auth.entity.MemberConsent;
import jwhs.cheftoo.auth.enums.ConsentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface MemberConsentRepository extends JpaRepository<MemberConsent, UUID> {

        @Modifying(clearAutomatically = true, flushAutomatically = true)
        @Query("UPDATE MemberConsent c SET c.isLatest = false " +
                "WHERE c.member.memberId = :memberId " +
                "AND c.consentType = :type " +
                "AND c.isLatest = true")

        int clearLatest(@Param("memberId") UUID memberId, @Param("type") ConsentType type);


}
