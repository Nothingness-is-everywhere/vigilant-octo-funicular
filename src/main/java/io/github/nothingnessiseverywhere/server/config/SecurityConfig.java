package io.github.nothingnessiseverywhere.server.config;

import io.github.nothingnessiseverywhere.server.filter.StaticResourceFilter;
import io.github.nothingnessiseverywhere.server.utils.PasswordEncoderUtil;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;

@Configuration
public class SecurityConfig {
    //IiNCAIw=

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationFailureHandler customAuthenticationFailureHandler;

    public SecurityConfig(UserDetailsService userDetailsService, @Lazy PasswordEncoder passwordEncoder, AuthenticationFailureHandler customAuthenticationFailureHandler) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return CookieCsrfTokenRepository.withHttpOnlyFalse();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(csrfTokenRepository())
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/css/determine.css", "/css/font.css", "/js/login.js",
                                "/font/**",
                                "/images/favicon.ico", "/images/login1.webp","/images/login2.jpg",
                                "/register", "/login")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/home")
                        .failureHandler(customAuthenticationFailureHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true) // 使会话失效
                        .deleteCookies("JSESSIONID") // 删除会话相关的 Cookie
                )
                .authenticationManager(authenticationManager());

        return http.build();
    }

    @Bean
    // 创建一个AuthenticationManager对象
    public AuthenticationManager authenticationManager() {
        // 创建一个DaoAuthenticationProvider对象
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // 设置userDetailsService
        provider.setUserDetailsService(userDetailsService);
        // 设置passwordEncoder
        provider.setPasswordEncoder(passwordEncoder);
        // 返回一个ProviderManager对象
        return new ProviderManager(provider);
    }

    @Bean
    // 定义一个名为passwordEncoder的Bean，类型为PasswordEncoder
    public PasswordEncoder passwordEncoder(PasswordEncoderUtil passwordEncoderUtil) {
        // 使用PasswordEncoderUtil类中的方法对密码进行编码和匹配
        return new PasswordEncoder() {
            @Override
            // 对原始密码进行编码
            public String encode(CharSequence rawPassword) {
                return passwordEncoderUtil.encodePassword(rawPassword.toString());
            }

            @Override
            // 对原始密码和编码后的密码进行匹配
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return passwordEncoderUtil.matchesPassword(rawPassword.toString(), encodedPassword);
            }
        };
    }

    @Bean
    // 注册静态资源过滤器
    public FilterRegistrationBean<StaticResourceFilter> staticResourceFilterRegistration() {
        // 创建过滤器注册对象
        FilterRegistrationBean<StaticResourceFilter> registration = new FilterRegistrationBean<>();
        // 设置过滤器
        registration.setFilter(new StaticResourceFilter(csrfTokenRepository()));
        // 设置过滤的URL模式
        registration.addUrlPatterns("/static/**");
        // 设置过滤器名称
        registration.setName("staticResourceFilter");
        // 设置过滤器顺序
        registration.setOrder(1);
        // 返回过滤器注册对象
        return registration;
    }

}