package com.blog.application.blogApplication.RestController;

import com.blog.application.blogApplication.DTO.PostFilterDto;
import com.blog.application.blogApplication.DTO.PostRequest;
import com.blog.application.blogApplication.Model.Post;
import com.blog.application.blogApplication.Service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController("PostRestController")
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
   private final PostService postService;

   @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
   @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostRequest postRequest){
       Post post = this.postService.createPost(postRequest.toPost(), postRequest.getTagIds());
       return new ResponseEntity<>(post, HttpStatus.CREATED);
   }

   @GetMapping
    public ResponseEntity<Page<Post>> getAllPosts(@ModelAttribute PostFilterDto postFilterDto){
       Page<Post> pagePost = this.postService.getAllPosts(postFilterDto);
       return new ResponseEntity<>(pagePost, HttpStatus.OK);
   }

   @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable("id") Long id){
       Post post = this.postService.getPostById(id);
       return new ResponseEntity<>(post, HttpStatus.OK);
   }

   @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
   @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable("id") Long id, @RequestBody PostRequest postRequest){
       Post post = this.postService.updatePost(id, postRequest.toPost(),postRequest.getTagIds());
       return new ResponseEntity<>(post, HttpStatus.OK);
   }

   @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
   @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostById(@PathVariable("id") Long id){
       this.postService.deletePost(id);
       return ResponseEntity.noContent().build();
   }
}
