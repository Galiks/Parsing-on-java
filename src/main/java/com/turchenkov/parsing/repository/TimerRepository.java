package com.turchenkov.parsing.repository;

import com.turchenkov.parsing.domains.Timer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimerRepository extends CrudRepository<Timer, Long> {
}
