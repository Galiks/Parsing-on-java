package com.turchenkov.parsing.listener;

import com.turchenkov.parsing.domains.shop.LetyShops;
import com.turchenkov.parsing.domains.shop.Shop;
import com.turchenkov.parsing.domains.user.Role;
import com.turchenkov.parsing.domains.user.User;
import com.turchenkov.parsing.repository.ShopRepository;
import com.turchenkov.parsing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class InitialDataListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        User admin = new User((long) 1, "admin", passwordEncoder.encode("admin"), true, Collections.singleton(Role.ADMIN));
        User user = new User((long) 2, "customer", passwordEncoder.encode("customer"), true, Collections.singleton(Role.USER));

        userRepository.save(admin);
        userRepository.save(user);
    }
}
