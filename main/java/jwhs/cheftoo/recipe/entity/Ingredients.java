package jwhs.cheftoo.recipe.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;


// 재료 엔티티
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Ingredients")
@Builder
public class Ingredients {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="ingredientsId")
    private UUID ingredientsId;

    @Column(name="recipeId")
    private UUID recipeId;

    @Column(name="ingredientsName", length = 100)
    private String ingredientsName;

    @Column(name="DataCreated")
    private String dataCreated;

    @Column(name="DataUpdated")
    private String dataUpdated;

}
