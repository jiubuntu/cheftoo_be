package jwhs.cheftoo.sauce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SauceRequestSaveDto {

    private String sauceName;
    private String quantity;
}
