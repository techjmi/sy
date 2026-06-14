package com.blog.application.blogApplication.Service.Impl;

import com.blog.application.blogApplication.DTO.RegisterRequest;
import com.blog.application.blogApplication.Enum.Role;
import com.blog.application.blogApplication.Model.User;
import com.blog.application.blogApplication.Repository.UserRepository;
import com.blog.application.blogApplication.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterRequest request) {

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.AUTHOR)
                .build();

        this.userRepository.save(user);

    }
}
