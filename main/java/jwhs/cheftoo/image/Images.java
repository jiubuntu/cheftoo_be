package jwhs.cheftoo.image;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;


// 대표이미지 저장
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Images")
@Builder
public class Images {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="imageId")
    private UUID imageId;

    @Column(name="recipeId")
    private UUID recipeId;

    @Column(name="memberId")
    private UUID memberId;

    @Column(name="imgPath", length = 255)
    private String imgPath;

    @Column(name="DataCreated")
    private LocalDateTime dataCreated;

    @Column(name="DataUpdated")
    private LocalDateTime dataUpdated;


}
