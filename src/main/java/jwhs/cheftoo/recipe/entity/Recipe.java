package jwhs.cheftoo.recipe.entity;

import jakarta.persistence.*;
import jwhs.cheftoo.auth.entity.Member;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.LocalDateTime;
import java.util.UUID;

// 레시피
@Entity
@Table(name="Recipe",
        indexes = {
            @Index(name = "idx_member_id", columnList = "memberId"),
            @Index(name = "idx_recipe_title", columnList = "recipeTitle"),
            @Index(name = "idx_member_recipe_title", columnList = "memberId, recipeTitle")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="RecipeId",updatable = false, nullable = false)
    private UUID recipeId;

    @ManyToOne
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @Column(name="RecipeTitle", length=100)
    private String recipeTitle;

    @Column(name="RecipeContent", columnDefinition = "TEXT")
    private String recipeContent;

    @CreationTimestamp
    @Column(name="DataCreated")
    private LocalDateTime dataCreated;

    @UpdateTimestamp
    @Column(name="DataUpdated")
    private LocalDateTime dataUpdated;
}
