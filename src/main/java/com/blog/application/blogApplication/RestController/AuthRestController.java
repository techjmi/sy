package com.blog.application.blogApplication.RestController;

import com.blog.application.blogApplication.DTO.LoginRequest;
import com.blog.application.blogApplication.DTO.LoginResponse;
import com.blog.application.blogApplication.DTO.RegisterRequest;
import com.blog.application.blogApplication.Jwt.JwtUtils;
import com.blog.application.blogApplication.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("User Registered");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        ));

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();

        String jwt =
                jwtUtils.generateTokenFromUsername(userDetails);

        List<String> roles =
                userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList();

        return ResponseEntity.ok(new LoginResponse(jwt, userDetails.getUsername(), roles)
        );
    }
}
