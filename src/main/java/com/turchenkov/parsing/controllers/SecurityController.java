package com.turchenkov.parsing.controllers;

import com.turchenkov.parsing.domains.user.Role;
import com.turchenkov.parsing.domains.user.User;
import com.turchenkov.parsing.repository.RoleRepository;
import com.turchenkov.parsing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Component
public class SecurityController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

//    @GetMapping("")
//    public String start(Model model){
//        model.addAttribute("user", new User());
//        return "login";
//    }

    @GetMapping("/login")
    public String loginGet(){
        return "login";
    }

    @Transactional
    @PostMapping("/login")
    public String loginPost(@ModelAttribute User user){
        Role role = new Role("USER");
        role.getUsers().add(user);
        user.getRoles().add(role);
        roleRepository.save(role);
        userRepository.save(user);


        return "redirect:/shops";
    }
}
