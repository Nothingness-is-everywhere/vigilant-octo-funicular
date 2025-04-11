package io.github.nothingnessiseverywhere.server.mapper;

import io.github.nothingnessiseverywhere.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMapper extends JpaRepository<User, Long> {
    User findByUsername(String username);
}