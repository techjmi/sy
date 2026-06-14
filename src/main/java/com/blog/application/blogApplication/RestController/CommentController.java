package com.blog.application.blogApplication.RestController;

import com.blog.application.blogApplication.Model.Comment;
import com.blog.application.blogApplication.Service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController("CommentRestController")
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;


    @PostMapping("/post/{postId}/comment")
    public ResponseEntity<Comment> createComment(@PathVariable Long postId, @RequestBody Comment comment){
        Comment createdTag = this.commentService.createComment(postId, comment);
        return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Comment> getComment(@PathVariable Long id){
        Comment comment = this.commentService.getCommentById(id);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateCommentById(  @PathVariable Long id, @RequestBody Comment comment){
        Comment updatedComment = this.commentService.updateComment(id,comment);
        return new ResponseEntity<>(updatedComment,HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable("id") Long id){
        this.commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

}
