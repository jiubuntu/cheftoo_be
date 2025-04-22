package jwhs.cheftoo.recipe.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;


import java.time.LocalDateTime;
import java.util.UUID;

// 레시피
@Entity
@Table(name="Recipe")
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

    @Column(name="memberId")
    private UUID memberId;

    @Column(name="RecipeTitle", length=100)
    private String recipeTitle;

    @Column(name="RecipeContent", columnDefinition = "TEXT")
    private String recipeContent;

    @Column(name="DataCreated")
    private LocalDateTime dataCreated;

    @Column(name="DataUpdated")
    private LocalDateTime dataUpdated;
}
