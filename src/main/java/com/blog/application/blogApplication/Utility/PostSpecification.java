package com.blog.application.blogApplication.Utility;

import com.blog.application.blogApplication.Model.Post;
import com.blog.application.blogApplication.Model.Tag;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class PostSpecification {

    public static Specification<Post> search(String keyword) {
        Specification<Post> specification = (root, query, cb) -> {
            query.distinct(true);
            Predicate finalPredicate = cb.conjunction();
            if (keyword != null && !keyword.isBlank()) {
                String pattern = "%" + keyword.toLowerCase() + "%";
                Join<Post, Tag> tagJoin = root.join("tags", JoinType.LEFT);
                Predicate titlePredicate   = cb.like(cb.lower(root.get("title")),   pattern);
                Predicate contentPredicate = cb.like(cb.lower(root.get("content")), pattern);
                Predicate excerptPredicate = cb.like(cb.lower(root.get("excerpt")), pattern);
                Predicate authorPredicate  = cb.like(cb.lower(root.get("author")),  pattern);
                Predicate tagPredicate     = cb.like(cb.lower(tagJoin.get("name")), pattern);
                finalPredicate = cb.or(titlePredicate, contentPredicate, excerptPredicate, authorPredicate, tagPredicate);
            }
            return finalPredicate;
        };
        return specification;

//        return (root, query, cb) -> {
//            query.distinct(true);
//            if(keyword == null || keyword.isBlank()) {
//                return cb.conjunction(); //means no filter return null
//            }
//            String pattern = "%" + keyword.toLowerCase() + "%";
//            Join<Post, Tag> tagJoin = root.join("tags", JoinType.LEFT);
//            return cb.or(cb.like(cb.lower(root.get("title")), pattern),
//                    cb.like(cb.lower(root.get("content")), pattern),
//                    cb.like(cb.lower(root.get("excerpt")), pattern),
//                    cb.like(cb.lower(root.get("author")), pattern),
//                    cb.like(cb.lower(tagJoin.get("name")), pattern)
//            );
//        };
    }
}
