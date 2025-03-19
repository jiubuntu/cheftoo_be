package jwhs.cheftoo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;


import java.util.UUID;

// 레시피
@Entity
@Table(name="Recipe")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="RecipeId",updatable = false, nullable = false)
    private UUID recipeId;

    @Column(name="memberId")
    private UUID memberId;

    @Column(name="FolderId")
    private UUID folderId;

    @Column(name="RecipeTitle", length=100)
    private String recipeTitle;

    @Column(name="RecipeContent", columnDefinition = "TEXT")
    private String recipeContent;

    @Column(name="RecipeStarScope")
    private float recipeStarScope;

    @Column(name="DataCreated")
    private String dataCreated;

    @Column(name="DataUpdated")
    private String dataUpdated;
}
