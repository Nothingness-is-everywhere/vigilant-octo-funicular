package io.github.nothingnessiseverywhere.server.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import io.github.nothingnessiseverywhere.server.utils.RandomValueGenerator;

public class StaticResourceFilter implements Filter {

    private static final String RANDOM_COOKIE_NAME = "STATIC_RESOURCE_RANDOM";
    private final CsrfTokenRepository csrfTokenRepository;

    public StaticResourceFilter(CsrfTokenRepository csrfTokenRepository) {
        this.csrfTokenRepository = csrfTokenRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 获取请求携带的 CSRF 令牌
        String requestCsrfToken = getCsrfTokenFromRequest(httpRequest);

        // 从服务器获取存储的 CSRF 令牌
        CsrfToken serverCsrfToken = csrfTokenRepository.loadToken(httpRequest);

        // 验证 CSRF 令牌
        if (requestCsrfToken == null || serverCsrfToken == null ||
                !requestCsrfToken.equals(serverCsrfToken.getToken())) {
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{\"error\": \"CSRF token validation failed\"}");
            return;
        }

        // 验证随机 Cookie
        String randomCookieValue = getRandomCookieValue(httpRequest);
        if (randomCookieValue == null) {
            // 生成新的随机 Cookie 值
            String newRandomValue = RandomValueGenerator.generateRandomValue(16);
            addRandomCookie(httpResponse, newRandomValue);
        } else {
            if (randomCookieValue.isEmpty()) {
                // 随机 Cookie 已过期，重新生成新的随机 Cookie 值
                String newRandomValue = RandomValueGenerator.generateRandomValue(16);
                addRandomCookie(httpResponse, newRandomValue);
                return;
            }
            if ("5".equals(randomCookieValue)) {
                // 随机 Cookie 值不匹配，拒绝访问
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid random cookie value");
                return;
            }
            // 验证随机 Cookie 值，这里简单假设验证通过，可根据需求添加复杂逻辑
            // 一次性使用后，下次请求需要新的随机 Cookie
            addRandomCookie(httpResponse, null);
        }
        String referer = httpRequest.getHeader("Referer");
        String requestURI = httpRequest.getRequestURI();

        // 仅对静态资源路径进行校验（如 /static/**）
        if (requestURI.startsWith("/static/**")) {
            // 校验 Referer 是否合法
            if (!isValidReferer(referer)) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "非法资源访问");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isValidReferer(String referer) {
        // 允许的域名列表（支持正则表达式）
        String[] allowedDomains = {
                "http://192.168.1.249:8080", // 替换为你的域名
                "https?://localhost(:\\d+)?"
        };

        if (referer == null) {
            return true; // 根据需求调整：true 允许直接访问，false 拒绝
        }

        for (String domain : allowedDomains) {
            if (domain == null) continue;
            if (referer.matches(domain)) {
                return true;
            }
        }
        return false;
    }


    private String getCsrfTokenFromRequest(HttpServletRequest request) {
        // 先尝试从请求头获取 CSRF 令牌
        String csrfToken = request.getHeader("X-XSRF-TOKEN");
        if (csrfToken == null) {
            // 若请求头中没有，尝试从 Cookie 获取
            csrfToken = getCsrfTokenFromCookie(request);
        }
        return csrfToken;
    }

    private String getCsrfTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("XSRF-TOKEN".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private String getRandomCookieValue(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (RANDOM_COOKIE_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private void addRandomCookie(HttpServletResponse response, String value) {
        Cookie randomCookie = new Cookie(RANDOM_COOKIE_NAME, value);
        randomCookie.setPath("/");
        if (value == null) {
            randomCookie.setMaxAge(0); // 删除 Cookie
        } else {
            randomCookie.setMaxAge(60); // 设置有效期为 60 秒
        }
        response.addCookie(randomCookie);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        // 初始化方法，可留空
    }

    @Override
    public void destroy() {
        // 销毁方法，可留空
    }

}