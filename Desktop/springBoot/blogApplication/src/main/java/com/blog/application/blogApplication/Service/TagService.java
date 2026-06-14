package com.blog.application.blogApplication.Service;

import com.blog.application.blogApplication.Model.Tag;

import java.util.List;

public interface TagService {
     Tag createTag(Tag tag);
     List<Tag> getAllTags();
     Tag getTagById(Long id);
     Tag updateTag(Long id, Tag tag);
     void deleteTag(Long id);
}
