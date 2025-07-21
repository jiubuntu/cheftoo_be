package jwhs.cheftoo.cookingorder.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CookingOrderRequestSaveDto {
    @JsonProperty("order")
    private long order;
    @JsonProperty("content")
    private String content;
    @JsonProperty("cookingOrderImageKey")
    private String cookingOrderImageKey;
}
