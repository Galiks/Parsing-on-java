package com.turchenkov.parsing;

import com.turchenkov.parsing.model.User;
import com.turchenkov.parsing.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Connection;

@SpringBootApplication
public class ParsingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParsingApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner commandLineRunner(UserRepository userRepository){
//		return args -> {
//			User user = new User("Pavel", 21);
//            userRepository.save(user);
//		};
//	}
}
