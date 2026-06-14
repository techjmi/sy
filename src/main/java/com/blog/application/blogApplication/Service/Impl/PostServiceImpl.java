package com.blog.application.blogApplication.Service.Impl;

import com.blog.application.blogApplication.DTO.PostFilterDto;
import com.blog.application.blogApplication.Enum.Role;
import com.blog.application.blogApplication.Exception.ResourceNotFoundException;
import com.blog.application.blogApplication.Model.Post;
import com.blog.application.blogApplication.Model.Tag;
import com.blog.application.blogApplication.Model.User;
import com.blog.application.blogApplication.Repository.PostRepository;
import com.blog.application.blogApplication.Repository.TagRepository;
import com.blog.application.blogApplication.Repository.UserRepository;
import com.blog.application.blogApplication.Service.PostService;
import com.blog.application.blogApplication.Utility.PostSpecification;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;


    @Override
    public Page<Post> getAllPosts(PostFilterDto postFilterDto) {
        Sort sort = postFilterDto.getOrder().equalsIgnoreCase("asc")
                        ? Sort.by(postFilterDto.getSortField()).ascending()
                        : Sort.by(postFilterDto.getSortField()).descending();

        Pageable pageable = PageRequest.of(postFilterDto.getPageNo()-1, postFilterDto.getPageSize(), sort);
        Specification<Post> spec = Specification.where(PostSpecification.search(postFilterDto.getSearch()));
        if(postFilterDto.getAuthor() != null && !postFilterDto.getAuthor().isBlank()) {
            Specification<Post> authorSpec = (root, query, cb) -> {
                Predicate authorPredicate = cb.equal(root.get("author"), postFilterDto.getAuthor());
                return authorPredicate;
            };
            spec = spec.and(authorSpec);
        }

        if(postFilterDto.getTagId() != null) {
            Specification<Post> tagSpec = (root, query, cb) -> {
                Join<Post, Tag> tagJoin = root.join("tags");
                Predicate tagPredicate = cb.equal(tagJoin.get("tagId"), postFilterDto.getTagId());
                return tagPredicate;
            };
            spec = spec.and(tagSpec);
//            spec = spec.and((root, query, cb) ->
//                            cb.equal(root.join("tags").get("id"),
//                                    postFilterDto.getTagId()));
        }

        if(postFilterDto.getPublishedDate() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.function(
                                    "DATE",
                                    java.sql.Date.class,
                                    root.get("publishedAt")
                            ),
                            postFilterDto.getPublishedDate()
                    )
            );
        }

        Page<Post> posts = postRepository.findAll(spec, pageable);
        return posts;
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
    }
    @Override
    public Post createPost(Post post, List<Long> tagIds) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userRepository.findByEmail(auth.getName()).orElseThrow();

        if(loggedInUser.getRole() == Role.AUTHOR){
            post.setAuthor(loggedInUser.getName());
        }
        post.setUser(loggedInUser);
        if(tagIds != null && !tagIds.isEmpty()){
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(tagIds));
            post.setTags(tags);
        }
        return postRepository.save(post);
    }


    @Override
    public Post updatePost(Long id, Post post, List<Long> tagIds) {
        Post existingPost = getPostById(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userRepository.findByEmail(auth.getName()).orElseThrow();

        boolean isOwner = existingPost.getUser()
                        .getEmail()
                        .equals(auth.getName());
        boolean isAdmin = loggedInUser.getRole() == Role.ADMIN;
        if(!isOwner && !isAdmin){
            throw new AccessDeniedException("You are not allowed to modify this post.");
        }

        existingPost.setTitle(post.getTitle());
        existingPost.setExcerpt(post.getExcerpt());
        existingPost.setContent(post.getContent());
        existingPost.setPublishedAt(post.getPublishedAt());
        existingPost.setIsPublished(post.getIsPublished());

        if(isAdmin){
            existingPost.setAuthor(post.getAuthor());
        }
        if (tagIds != null) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(tagIds));
            existingPost.setTags(tags);
        }
        return postRepository.save(existingPost);
    }

    @Override
    public void deletePost(Long id) {
        Post existingPost = getPostById(id);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isOwner = existingPost.getUser().getEmail().equals(auth.getName());
        User loggedInUser = userRepository.findByEmail(auth.getName()).orElseThrow();
        boolean isAdmin = loggedInUser.getRole() == Role.ADMIN;

        if(!isOwner && !isAdmin){
            throw new AccessDeniedException("You are not allowed to delete this post.");
        }
        postRepository.delete(existingPost);
    }
}
