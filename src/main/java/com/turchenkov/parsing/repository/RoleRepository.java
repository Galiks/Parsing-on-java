package com.turchenkov.parsing.repository;

import com.turchenkov.parsing.domains.user.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
}
