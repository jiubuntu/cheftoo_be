package jwhs.cheftoo.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponseDto {
    private String nickName;

    private String commentContent;

    @JsonFormat(pattern = "yy-MM-dd HH:mm")
    private LocalDateTime dataCreated;
}
