package com.blog.application.blogApplication.Controller;

import com.blog.application.blogApplication.DTO.RegisterRequest;
import com.blog.application.blogApplication.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model){
        model.addAttribute("user", new RegisterRequest());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterRequest request){
        authService.register(request);
        return "redirect:/login";
    }

}
