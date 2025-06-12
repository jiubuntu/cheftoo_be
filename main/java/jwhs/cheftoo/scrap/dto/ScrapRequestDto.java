package jwhs.cheftoo.scrap.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ScrapRequestDto {
    private UUID scrapId;
    private String scrapName;
}
