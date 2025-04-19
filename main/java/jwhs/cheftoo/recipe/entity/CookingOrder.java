package jwhs.cheftoo.recipe.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

// 조리순서
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "CookingOrder")
public class CookingOrder {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "CookingOrderId")
    private UUID cookingOrderId;

    @Column(name = "recipeId")
    private UUID recipeId;

    @Column(name = "order")
    private long order;

    @Column(name = "content")
    private String content;

    @Column(name = "imgPath", length = 255)
    private String imgPath;

    @Column(name="DataCreated")
    private LocalDateTime dataCreated;

    @Column(name="DataUpdated")
    private LocalDateTime dataUpdated;

}
