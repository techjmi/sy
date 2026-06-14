package com.blog.application.blogApplication.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String email;
    private String password;
}
