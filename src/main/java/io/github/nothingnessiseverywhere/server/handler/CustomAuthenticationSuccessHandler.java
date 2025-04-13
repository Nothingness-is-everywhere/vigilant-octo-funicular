package io.github.nothingnessiseverywhere.server.handler;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    // 实现AuthenticationSuccessHandler接口，重写onAuthenticationSuccess方法
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        // 获取认证成功的用户名
        String username = authentication.getName();
        HttpSession session = request.getSession();
        session.setAttribute("username", username);
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
