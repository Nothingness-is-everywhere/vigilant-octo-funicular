package io.github.nothingnessiseverywhere.server.handler;

import io.github.nothingnessiseverywhere.server.entity.User;
import io.github.nothingnessiseverywhere.server.mapper.UserMapper;
import io.github.nothingnessiseverywhere.server.utils.AESEncryptionUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final UserMapper userMapper;

    public CustomAuthenticationSuccessHandler(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    // 实现AuthenticationSuccessHandler接口，重写onAuthenticationSuccess方法
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        // 获取认证成功的用户名
        String username = authentication.getName();
        // 使用AES加密用户名
        String encryptedUsername = AESEncryptionUtil.encrypt(username);
        User user = userMapper.findByUsername(encryptedUsername);
        user.setUsername(username);
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        // 判断用户名是否为root
        if ("root".equals(username)) {
            // 如果是root用户，重定向到/admin页面
            response.sendRedirect("/admin");
        } else {
            // 否则，重定向到/home页面
            response.sendRedirect("/home");
        }
    }
}
