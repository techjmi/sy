package com.blog.application.blogApplication.DTO;

import com.blog.application.blogApplication.Model.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostRequest {
    private String title;
    private String excerpt;
    private String content;
    private String author;
    private Boolean isPublished;
    private List<Long> tagIds;

    public Post toPost() {
        Post post = new Post();
        post.setTitle(this.title);
        post.setExcerpt(this.excerpt);
        post.setContent(this.content);
        post.setAuthor(this.author);
        post.setIsPublished(this.isPublished);
        return post;
    }
}
