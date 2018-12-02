package com.turchenkov.parsing;

import com.turchenkov.parsing.model.Shop;
import com.turchenkov.parsing.model.User;
import com.turchenkov.parsing.parsing.ParserInterface;
import com.turchenkov.parsing.parsing.ParsingAllSite;
import com.turchenkov.parsing.repository.ShopRepository;
import com.turchenkov.parsing.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.ArrayList;

@SpringBootApplication
public class ParsingApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(ParsingApplication.class, args);
    }
}
