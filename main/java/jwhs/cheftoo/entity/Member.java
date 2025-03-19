package jwhs.cheftoo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name="Member")
public class Member {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "MemberId", updatable = false, nullable = false)
    private UUID memberId;

    @Column(name = "KakaoId")
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

    @Column(name = "DataCreated")
    private String dataCreated;

    @Column(name = "DataUpdated")
    private String dataUpdated;

}
