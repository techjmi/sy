package com.blog.application.blogApplication.Service.Impl;

import com.blog.application.blogApplication.Exception.ResourceNotFoundException;
import com.blog.application.blogApplication.Model.Comment;
import com.blog.application.blogApplication.Model.Post;
import com.blog.application.blogApplication.Model.User;
import com.blog.application.blogApplication.Repository.CommentRepository;
import com.blog.application.blogApplication.Repository.PostRepository;
import com.blog.application.blogApplication.Repository.UserRepository;
import com.blog.application.blogApplication.Service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

   private final CommentRepository commentRepository;
   private final PostRepository postRepository;
   private final UserRepository userRepository;


    @Override
    public Comment createComment(Long postId, Comment comment) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new ResourceNotFoundException("Post not found with id: " + postId));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            User user = userRepository.findByEmail(auth.getName()).orElseThrow();
            comment.setUser(user);
        }
        comment.setPost(post);
        return commentRepository.save(comment);
    }

    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
    }

    @Override
    public Comment updateComment(Long id, Comment comment) {
        Comment existingComment = getCommentById(id);
        this.validateCommentAccess(existingComment);
        existingComment.setName(comment.getName());
        existingComment.setEmail(comment.getEmail());
        existingComment.setComment(comment.getComment());
        return commentRepository.save(existingComment);
    }

    @Override
    public void deleteComment(Long id) {
        Comment existingComment = getCommentById(id);
        this.validateCommentAccess(existingComment);
        commentRepository.delete(existingComment);
    }

    private void validateCommentAccess(Comment comment) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        boolean commentOwner = comment.getUser() != null && comment.getUser().getEmail().equals(email);
        boolean postOwner = comment.getPost().getUser().getEmail().equals(email);
        boolean isAdmin = auth.getAuthorities().stream()
                        .anyMatch(a ->
                                a.getAuthority().equals("ROLE_ADMIN"));

        if (!commentOwner && !postOwner && !isAdmin) {
            throw new RuntimeException("Access denied");
        }
    }
}
