package com.turchenkov.parsing.config;

import com.turchenkov.parsing.domains.user.User;
import com.turchenkov.parsing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        if (s.equals("admin")) {
            return org.springframework.security.core.userdetails.User.withUsername("admin")
                    .roles("ADMIN")
                    .password("admin")
                    .accountExpired(false)
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(false)
                    .build();
        } else {
            User user = userRepository.findUserByUsername(s);
            if (user == null) {
                throw new UsernameNotFoundException(s);
            }
            return new MyUserPrincipal(user);
        }
    }
}
