package jwhs.cheftoo.sauce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SauceRequestSaveDto {
    @JsonProperty("sauceName")
    private String sauceName;

    @JsonProperty("quantity")
    private String quantity;
}
