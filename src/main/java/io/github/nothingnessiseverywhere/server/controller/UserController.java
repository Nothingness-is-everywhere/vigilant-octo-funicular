package io.github.nothingnessiseverywhere.server.controller;

import io.github.nothingnessiseverywhere.server.entity.User;
import io.github.nothingnessiseverywhere.server.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long userId) {
        // 创建一个Map对象，用于存储返回结果
        Map<String, Object> result = new HashMap<>();
        try {
            // 调用服务层方法删除用户
            boolean success = userService.deleteById(userId);
            if (success) {
                // 如果删除成功，将success和message放入result中
                result.put("success", true);
                result.put("message", "用户删除成功");
            } else {
                // 如果删除失败，将success和message放入result中
                result.put("success", false);
                result.put("message", "用户删除失败");
            }
            // 返回结果
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // 如果出现异常，将success和message放入result中，并返回500状态码
            result.put("success", false);
            result.put("message", "删除用户时出现错误：" + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    @PostMapping("/register")
    // 处理POST请求，注册用户
    public ResponseEntity<String> register(User user) {
        // 从请求体中获取用户信息
        if (userService.register(user.getUsername(), user.getPassword())) {
            // 调用userService的register方法，注册用户
            return ResponseEntity.ok("注册成功");
            // 返回注册成功的响应
        }
        return ResponseEntity.badRequest().body("用户名已存在");
        // 返回用户名已存在的响应
    }


}
