package com.blog.application.blogApplication.Service;

import com.blog.application.blogApplication.Model.Comment;

public interface CommentService {
    Comment createComment(Long postId, Comment comment);
    Comment getCommentById(Long id);
    Comment updateComment(Long id, Comment comment);
    void deleteComment(Long id);
}
