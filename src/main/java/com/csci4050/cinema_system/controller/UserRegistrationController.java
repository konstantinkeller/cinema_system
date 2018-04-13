package com.csci4050.cinema_system.controller;

import com.csci4050.cinema_system.dao.ConfirmationTokenRepository;
import com.csci4050.cinema_system.dao.UserRepository;
import com.csci4050.cinema_system.dto.UserRegistrationDto;
import com.csci4050.cinema_system.model.ConfirmationToken;
import com.csci4050.cinema_system.model.User;
import com.csci4050.cinema_system.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class UserRegistrationController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    MailSender mailSender;

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        return "register";
    }

    @PostMapping
    public String registerUser(@ModelAttribute("user") @Valid UserRegistrationDto userDto, BindingResult result) {
        User existing = userService.findByEmail(userDto.getEmail());
        if (existing != null){
            result.rejectValue("email", null, "An account registered with this email already exists!");
        }

        if (result.hasErrors()){
            return "register";
        }

        User newUser = userService.save(userDto);

        ConfirmationToken confToken = new ConfirmationToken(newUser.getId());
        confirmationTokenRepository.save(confToken);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Cinema System Registration Confirmation");
        message.setText("Thank you for registering with our Cinema E-Booking System!\n\nYour account details are as follows:\nUsername: " + userDto.getEmail() + "\nConfirmation Key: " + confToken.getToken());
        message.setTo(userDto.getEmail());
        message.setFrom("cinema@sigilhosting.com");
        try {
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/register?success";
    }

    @GetMapping("/confirm")
    public String showConfForm(Model model) {
        return "confirm_email";
    }

    @PostMapping("/confirm")
    public String confirmEmail(@RequestParam("email") String email, @RequestParam("token") String token, @RequestBody String r, BindingResult result) {
        User usr = userService.findByEmail(email);
        ConfirmationToken t = confirmationTokenRepository.findConfirmationTokenByUserId(usr.getId());

        if (t != null && !usr.getConfirmed()) {
            if (token.equals(t.getToken())) {
                usr.setConfirmed(true);
                userRepository.save(usr);
                confirmationTokenRepository.delete(t);

                SimpleMailMessage message = new SimpleMailMessage();
                message.setSubject("Cinema System Account Confirmed");
                message.setText("Your account has successfully been confirmed!");
                message.setTo(usr.getEmail());
                message.setFrom("cinema@sigilhosting.com");
                try {
                    mailSender.send(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return "redirect:/login";
            } else {
                result.reject(null, "Confirmation key is invalid.");
                return "confirm_email";
            }
        } else {
            result.reject(null, "User is already confirmed.");
            return "confirm_email";
        }
    }
}
