package io.github.nothingnessiseverywhere.server.controller;

import io.github.nothingnessiseverywhere.server.entity.User;
import io.github.nothingnessiseverywhere.server.service.UserService;
import io.github.nothingnessiseverywhere.server.utils.AESEncryptionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long userId, HttpServletRequest request) {
        // 创建一个Map对象，用于存储返回结果
        Map<String, Object> result = new HashMap<>();
        // 获取当前会话
        HttpSession session = request.getSession();
        // 获取当前会话中的用户名
        User nowuser = (User) session.getAttribute("user");
        if (userId.equals(nowuser.getUserId()) ^ Objects.equals(AESEncryptionUtil.decrypt(nowuser.getUsername()), "root")) {
            result.put("success", false);
            result.put("message", "删除用户时出现错误：权限不够！");
            return ResponseEntity.status(500).body(result);
        }
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
    public ResponseEntity<String> register(@RequestBody User user) {
        // 从请求体中获取用户信息
        if (userService.register(user.getUsername(), user.getPassword())) {
            // 调用userService的register方法，注册用户
            return ResponseEntity.ok("注册成功");
            // 返回注册成功的响应
        }
        return ResponseEntity.badRequest().body("用户名已存在");
        // 返回用户名已存在的响应
    }

    @GetMapping("/getUser/{userId}")
    public ResponseEntity<List<User>> getUser(@PathVariable Long userId, HttpServletRequest request) {
        // 获取用户的逻辑
        // 获取当前会话
        HttpSession session = request.getSession();
        // 获取当前会话中的用户名
        User nowuser = (User) session.getAttribute("user");
        if (nowuser.getUsername().equals("root")) {
            return ResponseEntity.ok(userService.getAllUsers());
        } else if (userId.equals(nowuser.getUserId())) {
            return ResponseEntity.ok(Collections.singletonList(userService.findByUserId(userId)));
        } else  return ResponseEntity.status(403).build();
    }

    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId, @RequestBody User user, HttpServletRequest request) {
        if (userId.equals(user.getUserId())) {
            return ResponseEntity.badRequest().body("用户ID不一致");
        }
        // 更新用户的逻辑
        // 获取当前会话
        HttpSession session = request.getSession();
        // 获取当前会话中的用户名
        User nowuser = (User) session.getAttribute("user");

        if (userId.equals(nowuser.getUserId()) ^ nowuser.getUsername().equals("root")) {
            userService.UpdateUser(user);
            return ResponseEntity.ok("用户信息更新成功");
        } else return ResponseEntity.badRequest().body("用户权限不足");
    }
//
//    @PatchMapping("/partialUpdateUser/{userId}")
//    public ResponseEntity<String> partialUpdateUser(@PathVariable Long userId, @RequestBody Map<String, Object> updates) {
//        // 部分更新用户的逻辑
//    }


}
