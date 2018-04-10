package com.csci4050.cinema_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    @RequestMapping("/register")
    public String register(Model model) {
        return "register";
    }
}
