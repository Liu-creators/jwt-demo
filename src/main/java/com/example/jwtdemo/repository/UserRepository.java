package com.example.jwtdemo.repository;

import com.example.jwtdemo.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {

    private final Map<String, User> users = new HashMap<>();

    public UserRepository() {
        // 初始化一些测试用户
        User user1 = new User("user1", "password1");
        user1.addRole("ROLE_USER");
        users.put(user1.getUsername(), user1);

        User admin = new User("admin", "adminpass");
        admin.addRole("ROLE_ADMIN");
        admin.addRole("ROLE_USER");
        users.put(admin.getUsername(), admin);
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }

    public User save(User user) {
        users.put(user.getUsername(), user);
        return user;
    }

    public void delete(User user) {
        users.remove(user.getUsername());
    }

    public boolean existsByUsername(String username) {
        return users.containsKey(username);
    }
}
