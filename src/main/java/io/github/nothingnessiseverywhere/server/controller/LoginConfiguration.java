package io.github.nothingnessiseverywhere.server.controller;

import io.github.nothingnessiseverywhere.server.entity.User;
import io.github.nothingnessiseverywhere.server.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

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

    @PostMapping("/login")
    public String login(@RequestBody User user, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("username", user.getUsername());
        if (user.getUsername().equals("root")) {
            return "admin";
        }
        return "home";
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        if (userService.register(user.getUsername(), user.getPassword())) {
            return ResponseEntity.ok("注册成功");
        }
        return ResponseEntity.badRequest().body("用户名已存在");
    }

    @GetMapping("/home")
    public String showHomePage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);
        return "home";
    }

    @GetMapping("/user")
    public String showUserPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);
        return "user";
    }

    @GetMapping("/admin")
    // 处理/admin请求，返回管理员页面
    public String showAdminPage(HttpServletRequest request, Model model) {
        // 获取当前会话
        HttpSession session = request.getSession();
        // 获取当前会话中的用户名
        String username = (String) session.getAttribute("username");

        // 如果用户名不是root，则返回home页面
        if (!username.equals("root")) {
            return "home";
        }
        List<User> users = userService.getAllUsers(); // 从数据库获取所有用户数据
        model.addAttribute("users", users); // 将用户数据添加到模型中
        // 返回admin页面
        return "admin";
    }
}
