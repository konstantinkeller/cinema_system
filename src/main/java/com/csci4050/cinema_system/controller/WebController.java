package com.csci4050.cinema_system.controller;

import com.csci4050.cinema_system.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/success")
    public String loginSuccess(Model model) {
        return "success";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }
}
