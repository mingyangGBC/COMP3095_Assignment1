package com.example.comp3095_assignment1.services;

import com.example.comp3095_assignment1.dto.RegisterDto;
import com.example.comp3095_assignment1.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService{
    User findByUsername(String username);
    User save(RegisterDto register);
}
