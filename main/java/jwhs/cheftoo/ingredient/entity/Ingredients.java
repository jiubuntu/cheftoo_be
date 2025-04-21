package jwhs.cheftoo.ingredient.entity;


import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

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

    @Column(name = "recipeId")
    private UUID recipeId;

    @Column(name = "ingredientsName" , length = 100)
    private String ingredientsName;

    @Column(name ="ingredientsNum", length = 10)
    private String ingredientsNum;


    @Column(name="DataCreated")
    private LocalDateTime dataCreated;

    @Column(name="DataUpdated")
    private LocalDateTime dataUpdated;

}
