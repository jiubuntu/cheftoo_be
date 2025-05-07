package jwhs.cheftoo.cookingorder.entity;


import jakarta.persistence.*;
import jwhs.cheftoo.recipe.entity.Recipe;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

// 조리순서
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "CookingOrder",
        indexes = {
            @Index(name = "idx_recipe_id", columnList = "recipeId"),
            @Index(name = "idx_recipe_order", columnList = "recipeId, order")
        }
)
public class CookingOrder {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "CookingOrderId")
    private UUID cookingOrderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipeId", nullable = false)
    private Recipe recipe;

    @Column(name = "`order`")
    private long order;

    @Column(name = "content")
    private String content;

    @Column(name = "imgPath", length = 255)
    private String imgPath;

    @CreationTimestamp
    @Column(name="DataCreated")
    private LocalDateTime dataCreated;

    @UpdateTimestamp
    @Column(name="DataUpdated")
    private LocalDateTime dataUpdated;

}
