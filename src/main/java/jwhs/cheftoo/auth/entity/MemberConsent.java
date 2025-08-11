package jwhs.cheftoo.auth.entity;

import jakarta.persistence.*;
import jwhs.cheftoo.auth.enums.ConsentType;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Table(name = "MemberConsent",
        indexes = {
                @Index(name="idx_member_type", columnList="memberId, consentType"),
                @Index(name="idx_member_latest", columnList="memberId, isLatest")
        })
public class MemberConsent {
    @Id
    @GeneratedValue
    private UUID memberConsentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "consentType", nullable = false, length = 20)
    private ConsentType consentType;

    @Column(name = "version", nullable = false, length = 20)
    private String version;

    @Column(name = "agreed", nullable = false)
    private boolean agreed;

    @Column(name = "agreedAt", nullable = false)
    private LocalDateTime agreedAt;

    @Column(name = "isLatest", nullable = false)
    private boolean isLatest;

}
