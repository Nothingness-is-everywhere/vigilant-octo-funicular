package io.github.nothingnessiseverywhere.server.mapper;

import io.github.nothingnessiseverywhere.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserMapper extends JpaRepository<User, Long> {
// 根据用户名查找用户
    User findByUsername(String username);
// 查找所有用户
    List<User> findAll();

// 根据用户ID删除用户
    void deleteById(Long userId);

// 根据用户ID查找用户
    User findByUserId(Long userId);


}