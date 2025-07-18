package jwhs.cheftoo.sauce.entity;

import jakarta.persistence.*;
import jwhs.cheftoo.recipe.entity.Recipe;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="Sauce",
        indexes = {
            @Index(name = "idx_recipe_id", columnList = "recipeId")
        }
)
@Builder
public class Sauce {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "sauceId")
    private UUID sauceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipeId")
    private Recipe recipe;

    @Column(name = "sauceName" , length = 100)
    private String sauceName;

    @Column(name ="quantity", length = 30)
    private String quantity;

    @CreationTimestamp
    @Column(name="DataCreated")
    private LocalDateTime dataCreated;

    @UpdateTimestamp
    @Column(name="DataUpdated")
    private LocalDateTime dataUpdated;


}
