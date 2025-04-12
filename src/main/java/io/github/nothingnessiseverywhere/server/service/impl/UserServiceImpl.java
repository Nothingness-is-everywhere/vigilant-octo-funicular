package io.github.nothingnessiseverywhere.server.service.impl;

import io.github.nothingnessiseverywhere.server.entity.User;
import io.github.nothingnessiseverywhere.server.mapper.UserMapper;
import io.github.nothingnessiseverywhere.server.service.UserService;
import io.github.nothingnessiseverywhere.server.utils.AESEncryptionUtil;
import io.github.nothingnessiseverywhere.server.utils.PasswordEncoderUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserMapper userMapper;
    private final PasswordEncoderUtil passwordEncoderUtil;

    public UserServiceImpl(UserMapper userMapper, PasswordEncoderUtil passwordEncoderUtil) {
        this.userMapper = userMapper;
        this.passwordEncoderUtil = passwordEncoderUtil;
    }

    @Override
    public boolean register(String username, String password) {
        String encryptedUsername = AESEncryptionUtil.encrypt(username);
        if (encryptedUsername == null) {
            return false;
        }
        if (userMapper.findByUsername(encryptedUsername) != null) {
            return false;
        }
        String encodedPassword = passwordEncoderUtil.encodePassword(password);
        User user = new User();
        user.setUsername(encryptedUsername);
        user.setPassword(encodedPassword);
        try {
            userMapper.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String rawUsername) throws UsernameNotFoundException {
        // 使用原有加密逻辑
        String encryptedUsername = AESEncryptionUtil.encrypt(rawUsername);
        if (encryptedUsername == null) {
            throw new UsernameNotFoundException("用户名加密失败");
        }

        // 根据加密后的用户名查找用户
        User user = userMapper.findByUsername(encryptedUsername);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 返回 Spring Security 需要的 UserDetails
        return org.springframework.security.core.userdetails.User
                .withUsername(rawUsername) // 使用原始用户名
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .build();
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.findAll();
    }
}