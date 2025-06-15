package jwhs.cheftoo.scrap.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
// Scrap Insert시 사용되는 DTO
public class ScrapInsertRequestDto {
    @JsonProperty("scrapName")
    private String scrapName;
}
