package com.csci4050.cinema_system.dao;

import org.springframework.data.repository.CrudRepository;

import com.csci4050.cinema_system.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);
}
