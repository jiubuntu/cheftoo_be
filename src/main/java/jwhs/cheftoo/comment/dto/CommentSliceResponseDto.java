package jwhs.cheftoo.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
@AllArgsConstructor
public class CommentSliceResponseDto<CommentResponseDetailDto> {
    private List<CommentResponseDetailDto> content;
    private int number;
    private int size;
    private boolean hasNext;

    public static CommentSliceResponseDto fromSlice(Slice slice) {
        return new CommentSliceResponseDto(
                slice.getContent(),
                slice.getNumber(),
                slice.getSize(),
                slice.hasNext()
        );
    }
}
