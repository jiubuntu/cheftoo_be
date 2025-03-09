package jwhs.cheftoo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name="Member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    @Column(name = "MemberId", updatable = false)
    private Long memberId;

    @Column(name = "KakaoId")
    private String kakaoId;

    @Column(name = "Nickname")
    private String nickname;

    @Column(name = "Name")
    private String name;

    @Column(name = "Email")
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
