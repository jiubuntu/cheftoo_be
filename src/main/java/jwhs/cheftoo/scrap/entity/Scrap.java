package jwhs.cheftoo.scrap.entity;

import jakarta.persistence.*;
import jwhs.cheftoo.auth.entity.Member;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "scrap",
        indexes = {
            @Index(name = "idx_member_id", columnList = "memberId")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Scrap {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ScrapId", updatable = false, nullable = false)
    private UUID scrapId;

    @ManyToOne
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @Column(name = "ScrapName", columnDefinition = "TEXT")
    private String scrapName;

    @CreationTimestamp
    @Column(name="DataCreated")
    private LocalDateTime dataCreated;

    @UpdateTimestamp
    @Column(name="DataUpdated")
    private LocalDateTime dataUpdated;

}
