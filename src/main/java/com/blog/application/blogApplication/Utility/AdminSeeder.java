package com.blog.application.blogApplication.Utility;

import com.blog.application.blogApplication.Enum.Role;
import com.blog.application.blogApplication.Model.User;
import com.blog.application.blogApplication.Repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeeder {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (!userRepository.existsByEmail("ashraf@gmail.com")) {
            User admin = User.builder()
                    .name("Ashraf")
                    .email("ashraf@gmail.com")
                    .password(passwordEncoder.encode("admin"))
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(admin);
        }
    }
}
