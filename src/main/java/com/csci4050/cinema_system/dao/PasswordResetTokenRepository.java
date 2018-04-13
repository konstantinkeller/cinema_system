package com.csci4050.cinema_system.dao;

import com.csci4050.cinema_system.model.PasswordResetToken;
import org.springframework.data.repository.CrudRepository;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Long> {
    PasswordResetToken findPasswordResetTokenByUserId(Long userId);
}
