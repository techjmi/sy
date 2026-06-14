package com.blog.application.blogApplication.Controller;

import com.blog.application.blogApplication.Model.Comment;
import com.blog.application.blogApplication.Service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller("CommentViewController")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;


    @PostMapping("/posts/{postId}/comments")
    public String createComment(@PathVariable Long postId, @RequestParam(required = false) String name, @RequestParam (required = false) String email, @RequestParam String comment) {
        Comment newComment = new Comment();
        newComment.setName(name);
        newComment.setEmail(email);
        newComment.setComment(comment);
        this.commentService.createComment(postId, newComment);
        return "redirect:/posts/" + postId;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/comments/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("comment", commentService.getCommentById(id));
        return "comments/edit";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/comments/update/{id}")
    public String updateComment(@PathVariable Long id, @RequestParam String name, @RequestParam String email, @RequestParam String comment) {
        Comment existingComment = this.commentService.getCommentById(id);
        existingComment.setName(name);
        existingComment.setEmail(email);
        existingComment.setComment(comment);
        Comment updated = this.commentService.updateComment(id, existingComment);
        return "redirect:/posts/" + updated.getPost().getPostId();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/comments/delete/{id}")
    public String deleteComment(@PathVariable Long id) {
        Comment comment = commentService.getCommentById(id);
        Long postId = comment.getPost().getPostId();
        commentService.deleteComment(id);
        return "redirect:/posts/" + postId;
    }

}
