package com.turchenkov.parsing.repository;

import com.turchenkov.parsing.domains.user.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

}
