package jwhs.cheftoo.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name="Member",
        indexes = {
            @Index(name = "idx_kakao_id", columnList = "kakaoId"),
            @Index(name = "idx_nickname", columnList = "nickname")
        }
)
public class Member {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "MemberId",updatable = false, nullable = false)
    private UUID memberId;

    @Column(name = "KakaoId", unique = true)
    private String kakaoId;

    @Column(name = "Nickname", length = 50)
    private String nickname;

    @Column(name = "Name")
    private String name;

    @Column(name = "Email", length = 100)
    private String email;

    @Column(name = "Password")
    private String password;

    @Column(name = "LastLogin")
    private LocalDateTime lastLogin;

    @CreationTimestamp
    @Column(name = "DataCreated")
    private LocalDateTime dataCreated;

    @UpdateTimestamp
    @Column(name = "DataUpdated")
    private LocalDateTime dataUpdated;

}
