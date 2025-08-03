package jwhs.cheftoo.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestSaveDto {
    @JsonProperty("commentContent")
    private String commentContent;

}
