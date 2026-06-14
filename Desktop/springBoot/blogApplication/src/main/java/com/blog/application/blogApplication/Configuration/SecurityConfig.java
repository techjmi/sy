package com.blog.application.blogApplication.Configuration;

import com.blog.application.blogApplication.Jwt.AuthEntryPointJwt;
import com.blog.application.blogApplication.Jwt.AuthTokenFilter;
import com.blog.application.blogApplication.Service.Impl.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final AuthEntryPointJwt unauthorizedHandler;
    private final AuthTokenFilter authTokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/posts",
                                "/posts/{id}",
                                "/css/**",
                                "/login",
                                "/register",
                                "/api/auth/**",
                                "/api/posts/**"
                        ).permitAll()
                .requestMatchers("/posts/*/comments")
                .permitAll()

                .requestMatchers("/api/comments/**")
                .permitAll()

                .anyRequest()
                .authenticated()
        )

                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(unauthorizedHandler))

                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/posts", true)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );
              http
                .csrf(csrf -> csrf.disable());
              http.addFilterBefore(authTokenFilter,
                     UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
}