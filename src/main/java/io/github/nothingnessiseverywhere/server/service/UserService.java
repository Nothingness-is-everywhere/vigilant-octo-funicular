package io.github.nothingnessiseverywhere.server.service;

import io.github.nothingnessiseverywhere.server.entity.User;

import java.util.List;

public interface UserService {
    boolean register(String username, String password);
    List<User> getAllUsers();

    boolean deleteById(Long userId);

    User findByUserId(Long userId);

    void UpdateUser(User user);
}