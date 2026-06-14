package com.blog.application.blogApplication.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PostFilterDto {
    private String search;
    private Long tagId;
    private  String author;
    private LocalDate publishedDate;
    private Integer pageNo = 1;
    private Integer pageSize = 10;
    private String sortField = "publishedAt";
    private String order = "desc";
}

