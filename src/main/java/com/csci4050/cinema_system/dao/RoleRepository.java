package com.csci4050.cinema_system.dao;

import com.csci4050.cinema_system.model.Role;
import com.csci4050.cinema_system.model.User;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findRoleByName(String name);
}
