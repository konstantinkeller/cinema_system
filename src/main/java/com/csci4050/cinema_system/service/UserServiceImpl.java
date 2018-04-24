package com.csci4050.cinema_system.service;

import com.csci4050.cinema_system.dao.RoleRepository;
import com.csci4050.cinema_system.dao.UserRepository;
import com.csci4050.cinema_system.dto.UserDto;
import com.csci4050.cinema_system.dto.UserRegistrationDto;
import com.csci4050.cinema_system.model.Role;
import com.csci4050.cinema_system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    public User findById(Long id) {
        return userRepository.findUserById(id);
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User update(UserDto userDto){
        User user = findById(userDto.getId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());

        if (!userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        if (userDto.isEmployee()) {
            user.setRoles(new LinkedList<>(Arrays.asList(roleRepository.findRoleByName("ROLE_USER"), roleRepository.findRoleByName("ROLE_ADMIN"))));
        } else {
            user.setRoles(new LinkedList<>(Arrays.asList(roleRepository.findRoleByName("ROLE_USER"))));
        }

        return userRepository.save(user);
    }

    public User save(UserDto userDto){
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        if (userDto.isEmployee()) {
            user.setRoles(new LinkedList<>(Arrays.asList(roleRepository.findRoleByName("ROLE_USER"), roleRepository.findRoleByName("ROLE_ADMIN"))));
        } else {
            user.setRoles(new LinkedList<>(Arrays.asList(roleRepository.findRoleByName("ROLE_USER"))));
        }

        return userRepository.save(user);
    }

    public User save(UserRegistrationDto registration){
        User user = new User();
        user.setFirstName(registration.getFirstName());
        user.setLastName(registration.getLastName());
        user.setEmail(registration.getEmail());
        user.setPassword(passwordEncoder.encode(registration.getPassword()));
        user.setRoles(Arrays.asList(roleRepository.findRoleByName("ROLE_USER")));
        user.setStreet(registration.getStreet());
        user.setCity(registration.getCity());
        user.setState(registration.getState());
        user.setZip(registration.getZip());
        user.setPhoneNum(registration.getPhoneNum());
        user.setCardType(registration.getCardType());
        user.setCardNum(registration.getCardNum());
        user.setCardExp(registration.getCardExp());
        user.setCardZip(registration.getCardZip());
        user.setPromos(registration.getPromos());
        return userRepository.save(user);
    }

    public void changeUserPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}