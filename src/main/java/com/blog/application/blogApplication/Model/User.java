package com.blog.application.blogApplication.Model;

import com.blog.application.blogApplication.Enum.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Integer userId;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}
