package com.csci4050.cinema_system.dao;

import com.csci4050.cinema_system.model.ConfirmationToken;
import org.springframework.data.repository.CrudRepository;

public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, Long> {
    ConfirmationToken findConfirmationTokenByUserId(Long userId);
}
