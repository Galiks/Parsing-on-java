package com.turchenkov.parsing.controllers;

import com.turchenkov.parsing.domains.user.Role;
import com.turchenkov.parsing.domains.user.User;
import com.turchenkov.parsing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/registration")
    public String registrationGet(){
        return "registration";
    }

    @PostMapping("/registration")
    public String registrationPost(User newUser, Model model){
        User user = userRepository.findUserByUsername(newUser.getUsername());
        if (user != null){
            model.addAttribute("message", "User exists");
            return "registration";
        }

        //newUser.setRoles(Collections.singleton(Role.USER));
        userRepository.save(newUser);
        return "redirect:/login";
    }
}
