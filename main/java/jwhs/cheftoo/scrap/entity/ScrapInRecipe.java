package jwhs.cheftoo.scrap.entity;

import jakarta.persistence.*;
import jwhs.cheftoo.recipe.entity.Recipe;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ScrapInRecipe",
        indexes = {
            @Index(name = "idx_scrap_id", columnList = "scrapId"),
            @Index(name = "idx_recipe_id", columnList = "recipeId")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScrapInRecipe {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ScrapInRecipeId", updatable = false, nullable = false)
    private UUID scrapInRecipeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scrapId")
    private Scrap scrap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipeId")
    private Recipe recipe;

    @CreationTimestamp
    @Column(name="DataCreated")
    private LocalDateTime dataCreated;

    @UpdateTimestamp
    @Column(name="DataUpdated")
    private LocalDateTime dataUpdated;
}
