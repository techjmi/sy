package com.blog.application.blogApplication.Service.Impl;

import com.blog.application.blogApplication.Model.Tag;
import com.blog.application.blogApplication.Repository.TagRepository;
import com.blog.application.blogApplication.Service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public Tag createTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public Tag getTagById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
    }

    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag existingTag = this.getTagById(id);
        existingTag.setName(tag.getName());
        return tagRepository.save(existingTag);
    }

    @Override
    public void deleteTag(Long id) {
        Tag tag = this.getTagById(id);
        tagRepository.delete(tag);
    }

}
