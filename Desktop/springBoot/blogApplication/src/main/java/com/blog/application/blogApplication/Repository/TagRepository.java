package com.blog.application.blogApplication.Repository;

import com.blog.application.blogApplication.Model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
}
