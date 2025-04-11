package io.github.nothingnessiseverywhere.server.controller;

import io.github.nothingnessiseverywhere.server.entity.User;
import io.github.nothingnessiseverywhere.server.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class LoginConfiguration {

    private final UserService userService;

    public LoginConfiguration(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public RedirectView redirectToLogin() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/login");
        return redirectView;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        if (userService.register(user.getUsername(), user.getPassword())) {
            return ResponseEntity.ok("注册成功");
        }
        return ResponseEntity.badRequest().body("用户名已存在");
    }

    @GetMapping("/home")
    public String showHomePage() {
        return "home";
    }
}
