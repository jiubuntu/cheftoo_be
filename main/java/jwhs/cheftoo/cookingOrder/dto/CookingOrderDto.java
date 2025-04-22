package jwhs.cheftoo.cookingOrder.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CookingOrderDto {
    private UUID cookingOrderId;
    private UUID recipeId;
    private long order;
    private String content;
    private String imgPath;
}
