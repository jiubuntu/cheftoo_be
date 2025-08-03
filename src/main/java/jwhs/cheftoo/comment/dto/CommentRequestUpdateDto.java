package jwhs.cheftoo.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestUpdateDto {
    @JsonProperty("commentId")
    private UUID commentId;
    @JsonProperty("commentContent")
    private String commentContent;
}
