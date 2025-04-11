package io.github.nothingnessiseverywhere.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private static final String DEFAULT_ERROR_MSG = "登录失败，请重试";
    private final ObjectMapper objectMapper;

    // 使用构造器注入确保 ObjectMapper 配置统一
    public CustomAuthenticationFailureHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        // 设置响应状态及编码
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // 根据异常类型确定错误信息
        String errorMessage = getErrorMessage(exception);

        // 构造错误响应体
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", errorMessage);

        // 序列化并返回错误信息
        try {
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e) {
            throw new IOException("Failed to write authentication failure response", e);
        }
    }

    /**
     * 根据异常类型返回具体错误提示
     */
    private String getErrorMessage(AuthenticationException exception) {
        if (exception instanceof BadCredentialsException) {
            return "用户名或密码错误";
        } else if (exception instanceof LockedException) {
            return "账户已被锁定，请联系管理员";
        } else if (exception instanceof DisabledException) {
            return "账户已被禁用";
        } else if (exception instanceof AccountExpiredException) {
            return "账户已过期";
        } else if (exception instanceof CredentialsExpiredException) {
            return "密码已过期";
        } else {
            return DEFAULT_ERROR_MSG; // 默认提示
        }
    }
}