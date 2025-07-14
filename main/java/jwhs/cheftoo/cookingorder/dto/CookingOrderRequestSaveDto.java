package jwhs.cheftoo.cookingorder.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CookingOrderRequestSaveDto {
    private long order;
    private String content;
    private String cookingOrderImageKey;
}
