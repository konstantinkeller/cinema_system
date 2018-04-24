package com.csci4050.cinema_system.service;

import com.csci4050.cinema_system.dto.UserDto;
import com.csci4050.cinema_system.dto.UserRegistrationDto;
import com.csci4050.cinema_system.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findById(Long id);
    User findByEmail(String email);
    User save(UserDto userDto);
    User update(UserDto userDto);
    User save(UserRegistrationDto registration);
    void changeUserPassword(User user, String password);
}
