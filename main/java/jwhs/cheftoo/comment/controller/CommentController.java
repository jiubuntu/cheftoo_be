package jwhs.cheftoo.comment.controller;

import jakarta.servlet.http.HttpServletRequest;
import jwhs.cheftoo.comment.dto.CommentRequestDto;
import jwhs.cheftoo.comment.dto.CommentResponseDto;
import jwhs.cheftoo.comment.service.CommentService;
import jwhs.cheftoo.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpRequest;
import java.util.List;
import java.util.UUID;

@RestController
public class CommentController {

    private CommentService commentService;
    private JwtUtil jwtUtil;

    public CommentController(CommentService commentService, JwtUtil jwtUtil) {

        this.commentService = commentService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("recipe/comment/{recipeId}")
    public ResponseEntity<List<CommentResponseDto>> getAllCommentByRecipe(@PathVariable("recipeId") UUID recipeId) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.findAllCommentByRecipe(recipeId));
    }

    // 댓글 저장
    @PostMapping("recipe/{recipeId}/comment")
    public ResponseEntity<CommentResponseDto> saveComment(
            HttpServletRequest request,
            @RequestBody CommentRequestDto dto,
            @PathVariable("recipeId") UUID recipeId
    ) {
        String token = jwtUtil.getTokenFromRequest(request);
        UUID memberId = jwtUtil.getMemberIdFromToken(token);

        return ResponseEntity.status(HttpStatus.OK).body(commentService.createComment(dto, memberId, recipeId));
    }

    // 댓글 삭제
    @DeleteMapping("recipe/comment/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable UUID commentId,
            HttpServletRequest request
    ) {
        String token = jwtUtil.getTokenFromRequest(request);
        UUID memberId = jwtUtil.getMemberIdFromToken(token);

        commentService.deleteComment(commentId, memberId);
        return ResponseEntity.ok().build();
    }


}
