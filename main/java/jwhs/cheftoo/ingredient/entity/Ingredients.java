package jwhs.cheftoo.ingredient.entity;


import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Ingredients")
@Builder
public class Ingredients {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ingredientsId")
    private UUID  ingredientsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipeId")
    private Recipe recipe;

    @Column(name = "ingredientsName" , length = 100)
    private String ingredientsName;

    @Column(name ="ingredientsNum", length = 10)
    private String ingredientsNum;

    @CreationTimestamp
    @Column(name="DataCreated")
    private LocalDateTime dataCreated;

    @UpdateTimestamp
    @Column(name="DataUpdated")
    private LocalDateTime dataUpdated;

}
