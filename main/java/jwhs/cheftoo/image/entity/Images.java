package jwhs.cheftoo.image.entity;


import jakarta.persistence.*;
import jwhs.cheftoo.auth.entity.Member;
import jwhs.cheftoo.recipe.entity.Recipe;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;


// 대표이미지 저장
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Images",
        indexes = {
            @Index(name = "idx_recipe_id", columnList = "recipeId"),
            @Index(name = "idx_member_id", columnList = "memberId"),
            @Index(name = "idx_member_recipe", columnList = "memberId, recipeId")
        }
)
@Builder
public class Images {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="imageId")
    private UUID imageId;

    @OneToOne
    @JoinColumn(name = "recipeId", nullable = false)
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @Column(name="imgPath", length = 255)
    private String imgPath;

    @CreationTimestamp
    @Column(name="DataCreated")
    private LocalDateTime dataCreated;

    @UpdateTimestamp
    @Column(name="DataUpdated")
    private LocalDateTime dataUpdated;


}
