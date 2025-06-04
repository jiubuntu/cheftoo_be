package jwhs.cheftoo.comment.controller;

import jwhs.cheftoo.comment.dto.CommentResponseDto;
import jwhs.cheftoo.comment.service.CommentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("recipe/comment/{recipeId}")
    public List<CommentResponseDto> getAllCommentByRecipe(@PathVariable UUID recipeId) {
        return commentService.findAllCommentByRecipe(recipeId);
    }



}
