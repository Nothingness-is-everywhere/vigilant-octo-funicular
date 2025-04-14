package io.github.nothingnessiseverywhere.server.mapper;

import io.github.nothingnessiseverywhere.server.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserMapper extends JpaRepository<User, Long> {
// 根据用户名查找用户
    User findByUsername(String username);
    List<User> findAll();

    void deleteById(Long userId);

}