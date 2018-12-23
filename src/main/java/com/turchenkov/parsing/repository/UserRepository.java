package com.turchenkov.parsing.repository;

import com.turchenkov.parsing.domains.user.User;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.Size;

public interface UserRepository extends CrudRepository<User, Long> {
    User findUserByLogin(@Size(min = 5, max = 50, message = "Incorrect Login") String login);
}
