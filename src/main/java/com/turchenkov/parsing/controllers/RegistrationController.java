package com.turchenkov.parsing.controllers;

import com.turchenkov.parsing.domains.user.User;
import com.turchenkov.parsing.exception.ThereIsNoSuchUserException;
import com.turchenkov.parsing.exception.ThereIsSuchUserException;
import com.turchenkov.parsing.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/registration")
    public String registrationGet() {
        return "registration";
    }

    @PostMapping("/registration")
    public String registrationPost(User newUser, Model model) {
        if ( userService.findUserByName(newUser.getUsername()) != null) {
            model.addAttribute("message", "User exists");
            throw new ThereIsSuchUserException();
        } else {
            userService.saveUser(newUser);
            return "redirect:/login";
        }
    }
}
