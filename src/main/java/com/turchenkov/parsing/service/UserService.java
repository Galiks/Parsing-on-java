package com.turchenkov.parsing.service;

import com.turchenkov.parsing.domains.user.User;

public interface UserService {
    void saveUser(User user);
    User findUserByName(String name);
}
