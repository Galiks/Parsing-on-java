package com.turchenkov.parsing;

import com.turchenkov.parsing.model.User;
import com.turchenkov.parsing.parsing.Cash4BrandsParser;
import com.turchenkov.parsing.parsing.EPN_Parser;
import com.turchenkov.parsing.parsing.MegaBonusParser;
import com.turchenkov.parsing.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.sql.Connection;

@SpringBootApplication
public class ParsingApplication {

	public static void main(String[] args) throws IOException, InterruptedException {
		//SpringApplication.run(ParsingApplication.class, args);
        EPN_Parser epnParser = new EPN_Parser();
        epnParser.parsing();
	}

}
