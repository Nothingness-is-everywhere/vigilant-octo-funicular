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
    // 注入UserMapper
    private final UserMapper userMapper;
    // 注入PasswordEncoderUtil
    private final PasswordEncoderUtil passwordEncoderUtil;

    // 构造函数，注入UserMapper和PasswordEncoderUtil
    public UserServiceImpl(UserMapper userMapper, PasswordEncoderUtil passwordEncoderUtil) {
        this.userMapper = userMapper;
        this.passwordEncoderUtil = passwordEncoderUtil;
    }

    // 注册用户
    @Override
    public boolean register(String username, String password) {
        // 使用AES加密用户名
        String encryptedUsername = AESEncryptionUtil.encrypt(username);
        // 如果加密失败，返回false
        if (encryptedUsername == null) {
            return false;
        }
        // 如果用户名已存在，返回false
        if (userMapper.findByUsername(encryptedUsername) != null) {
            return false;
        }
        // 使用PasswordEncoderUtil加密密码
        String encodedPassword = passwordEncoderUtil.encodePassword(password);
        // 创建User对象
        User user = new User();
        user.setUsername(encryptedUsername);
        user.setPassword(encodedPassword);
        try {
            // 保存User对象
            userMapper.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 根据用户名加载用户信息
    @Override
    public UserDetails loadUserByUsername(String rawUsername) throws UsernameNotFoundException {
        // 使用原有加密逻辑
        String encryptedUsername = AESEncryptionUtil.encrypt(rawUsername);
        // 如果加密失败，抛出UsernameNotFoundException异常
        if (encryptedUsername == null) {
            throw new UsernameNotFoundException("用户名加密失败");
        }

        // 根据加密后的用户名查找用户
        User user = userMapper.findByUsername(encryptedUsername);
        // 如果用户不存在，抛出UsernameNotFoundException异常
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

    // 获取所有用户
    @Override
    public List<User> getAllUsers() {
        return userMapper.findAll();
    }

    @Override
    public boolean deleteUser(int userId) {
        int rows = userMapper.deleteUser(userId);
        return rows > 0;
    }
}