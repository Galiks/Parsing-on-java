package com.turchenkov.parsing.repository;

import com.turchenkov.parsing.domains.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Size;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findUserByUsername(@Size(min = 5, max = 50, message = "Incorrect Login") String username);
}
