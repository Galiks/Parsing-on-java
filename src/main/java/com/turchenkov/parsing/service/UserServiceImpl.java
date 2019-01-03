package com.turchenkov.parsing.service;

import com.turchenkov.parsing.domains.user.Role;
import com.turchenkov.parsing.domains.user.User;
import com.turchenkov.parsing.exception.ThereIsNoSuchUserException;
import com.turchenkov.parsing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveUser(User user) {
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public User findUserByName(String name) {
        User user = userRepository.findUserByUsername(name);
        if (user != null) {
            return user;
        } else {
            throw new ThereIsNoSuchUserException();
        }
    }
}
