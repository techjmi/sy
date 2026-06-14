package com.blog.application.blogApplication.RestController;

import com.blog.application.blogApplication.Model.Tag;
import com.blog.application.blogApplication.Service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('ADMIN')")
@RestController("TagRestController")
@RequiredArgsConstructor
@RequestMapping("/api/tags")
public class TagController {
    private final TagService tagService;

    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag){
        Tag createdTag = this.tagService.createTag(tag);
        return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Tag>> getAllTag(){
        List<Tag> tagList = this.tagService.getAllTags();
        return new ResponseEntity<>(tagList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable Long id){
        return ResponseEntity.ok(tagService.getTagById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tag> updateTagById(@PathVariable("id") Long id,@RequestBody Tag tag){
        Tag updatedTag = this.tagService.updateTag(id,tag);
        return new ResponseEntity<>(updatedTag,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTagById(@PathVariable("id") Long id){
        this.tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
