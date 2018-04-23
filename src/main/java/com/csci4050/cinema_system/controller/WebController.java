package com.csci4050.cinema_system.controller;

import com.csci4050.cinema_system.dao.PasswordResetTokenRepository;
import com.csci4050.cinema_system.dao.UserRepository;
import com.csci4050.cinema_system.model.PasswordResetToken;
import com.csci4050.cinema_system.model.User;
import com.csci4050.cinema_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;

@Controller
public class WebController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private UserService userService;

    @Autowired
    MailSender mailSender;

    @GetMapping("/success")
    public String loginSuccess(Authentication authentication) {
        boolean admin = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

        if (admin) return "redirect:/admin";
        else return "success";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/forgot_pass")
    public String showForgotPass(Model model) {
        return "forgot_password";
    }

    @PostMapping("/forgot_pass")
    public String processForgotPass(@RequestParam("email") String email, @RequestBody String r, BindingResult result) {
        User usr = userService.findByEmail(email);

        PasswordResetToken exist = passwordResetTokenRepository.findPasswordResetTokenByUserId(usr.getId());
        if (exist != null) passwordResetTokenRepository.delete(exist);

        PasswordResetToken token = new PasswordResetToken(usr.getId());
        passwordResetTokenRepository.save(token);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Cinema System Password Reset");
        message.setText("Our records indicate you have requested a password reset. Here is your password reset confirmation key: " + token.getToken());
        message.setTo(usr.getEmail());
        message.setFrom("cinema@sigilhosting.com");
        try {
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/check_pass_reset";
    }

    @GetMapping("/check_pass_reset")
    public String showTokenEntry(Model model) {
        return "verify_token";
    }

    @PostMapping("/check_pass_reset")
    public String verifyResetToken(@RequestParam("email") String email, @RequestParam("token") String token) {
        User usr = userService.findByEmail(email);
        PasswordResetToken t = passwordResetTokenRepository.findPasswordResetTokenByUserId(usr.getId());

        if (t != null) {
            if (token.equals(t.getToken())) {
                Authentication auth = new UsernamePasswordAuthenticationToken(
                        usr, null, Arrays.asList(
                        new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
                SecurityContextHolder.getContext().setAuthentication(auth);
                passwordResetTokenRepository.delete(t);
                return "redirect:/reset_pass";
            }
        }

        return "verify_token";
    }

    @GetMapping("/reset_pass")
    public String showResetPass(Model model) {
        return "reset_password";
    }

    @PostMapping("/reset_pass")
    public String resetPass(@RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPass, @RequestBody String r, BindingResult result) {
        User usr = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (password.equals(confirmPass)) {
            userService.changeUserPassword(usr, password);
            return "redirect:/login";
        } else {
            result.reject(null, "Passwords do not match!");
            return "reset_pass";
        }
    }
}
