package com.example.jwtdemo.service;

import com.example.jwtdemo.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findByUsername(String username);
}
