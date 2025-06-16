package jwhs.cheftoo.scrap.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

// 스크랩에 있는 레시피 삭제요청 dto

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScrapInRecipeDeleteRequestDto {
    @JsonProperty("scrapId")
    private UUID scrapId;
    @JsonProperty("recipeIdList")
    private List<UUID> recipeIdList;



}
