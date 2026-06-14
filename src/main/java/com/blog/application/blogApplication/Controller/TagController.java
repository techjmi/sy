package com.blog.application.blogApplication.Controller;

import com.blog.application.blogApplication.Model.Tag;
import com.blog.application.blogApplication.Service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('ADMIN')")
@Controller("TagViewController")
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping
    public String getAllTags(Model model) {
        model.addAttribute("tags", tagService.getAllTags());
        return "tags/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("tag", new Tag());
        return "tags/create";
    }

    @PostMapping
    public String createTag(@ModelAttribute Tag tag) {
        tagService.createTag(tag);
        return "redirect:/tags";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("tag", tagService.getTagById(id));
        return "tags/edit";
    }

    @PostMapping("/update/{id}")
    public String updateTag(@PathVariable Long id, @ModelAttribute Tag tag) {
        tagService.updateTag(id, tag);
        return "redirect:/tags";
    }

    @PostMapping("/delete/{id}")
    public String deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return "redirect:/tags";
    }

}
