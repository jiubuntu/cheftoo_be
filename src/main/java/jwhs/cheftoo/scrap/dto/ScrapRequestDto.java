package jwhs.cheftoo.scrap.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScrapRequestDto {
    @JsonProperty("scrapId")
    private UUID scrapId;
    @JsonProperty("scrapName")
    private String scrapName;
}
