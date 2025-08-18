package jwhs.cheftoo.comment.controller;

import jakarta.servlet.http.HttpServletRequest;
import jwhs.cheftoo.comment.dto.*;
import jwhs.cheftoo.comment.service.CommentService;
import jwhs.cheftoo.util.JwtUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class CommentController {

    private CommentService commentService;
    private JwtUtil jwtUtil;

    public CommentController(CommentService commentService, JwtUtil jwtUtil) {

        this.commentService = commentService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("recipe/{recipeId}/comment/")
    public ResponseEntity<List<CommentResponseDto>> getAllCommentByRecipe(@PathVariable("recipeId") UUID recipeId) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.findAllCommentByRecipe(recipeId));
    }

    // 댓글 저장
    @PostMapping("recipe/{recipeId}/comment")
    public ResponseEntity<CommentResponseDto> saveComment(
            HttpServletRequest request,
            @RequestBody CommentRequestSaveDto dto,
            @PathVariable("recipeId") UUID recipeId
    ) {
        String token = jwtUtil.getAccessTokenFromRequest(request);
        UUID memberId = jwtUtil.getMemberIdFromToken(token);

        return ResponseEntity.status(HttpStatus.OK).body(commentService.createComment(dto, memberId, recipeId));
    }

    // 댓글 삭제
    @DeleteMapping("recipe/comment/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable("commentId") UUID commentId,
            HttpServletRequest request
    ) throws AccessDeniedException {
        String token = jwtUtil.getAccessTokenFromRequest(request);
        UUID memberId = jwtUtil.getMemberIdFromToken(token);

        commentService.deleteComment(commentId, memberId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/member/comment")
    public ResponseEntity<CommentSliceResponseDto<CommentResponseDetailDto>> getCommentByMember(
            HttpServletRequest request,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "12") int size
    ) {
        String accessToken = jwtUtil.getAccessTokenFromRequest(request);
        UUID memberId = jwtUtil.getMemberIdFromToken(accessToken);
        Pageable pageable = PageRequest.of(page, size);

        CommentSliceResponseDto<CommentResponseDetailDto> commentList = commentService.findAllCommentByMember(memberId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(commentList);
    }

    @PutMapping("/comment")
    public ResponseEntity<HttpStatus> updateComment(
            @RequestBody CommentRequestUpdateDto dto
    ) {
        commentService.updateComment(dto);

        return ResponseEntity.ok().build();
    }


}
