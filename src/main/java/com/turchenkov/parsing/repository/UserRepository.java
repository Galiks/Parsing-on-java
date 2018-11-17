package com.turchenkov.parsing.repository;

import com.turchenkov.parsing.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByName(String name);
}
