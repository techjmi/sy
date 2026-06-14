package com.blog.application.blogApplication.Controller;

import com.blog.application.blogApplication.DTO.PostFilterDto;
import com.blog.application.blogApplication.Model.Post;
import com.blog.application.blogApplication.Service.PostService;
import com.blog.application.blogApplication.Service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller("PostViewController")
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final TagService tagService;

    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("post", new Post());
        model.addAttribute("allTags", tagService.getAllTags());
        return "posts/create";
    }

    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @PostMapping
    public String createPost(@ModelAttribute Post post, @RequestParam(required = false) List<Long> tagIds) {
        postService.createPost(post, tagIds);
        return "redirect:/posts";
    }

    @GetMapping
    public String getAllPosts(@ModelAttribute PostFilterDto postFilterDto, Model model) {
        Page<Post> posts = postService.getAllPosts(postFilterDto);
        model.addAttribute("posts", posts);
        model.addAttribute("allTags", tagService.getAllTags());

        return "posts/list";
    }

    @GetMapping("/{id}")
    public String getPost(@PathVariable Long id, Model model) {
        model.addAttribute("post", this.postService.getPostById(id));
        return "posts/details";
    }

    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("post", this.postService.getPostById(id));
        model.addAttribute("allTags", tagService.getAllTags());
        return "posts/edit";
    }

    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @PostMapping("/update/{id}")
    public String updatePost(@PathVariable Long id, @ModelAttribute Post post, @RequestParam(required = false) List<Long> tagIds) {
        postService.updatePost(id, post, tagIds);
        return "redirect:/posts";
    }

    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/posts";
    }
}
