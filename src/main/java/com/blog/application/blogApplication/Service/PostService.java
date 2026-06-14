package com.blog.application.blogApplication.Service;

import com.blog.application.blogApplication.DTO.PostFilterDto;
import com.blog.application.blogApplication.Model.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {

    Page<Post> getAllPosts(PostFilterDto postFilterDto);

    Post getPostById(Long id);

    Post createPost(Post post, List<Long> tagIds);

    Post updatePost(Long id, Post post, List<Long> tagIds);

    void deletePost(Long id);
}
