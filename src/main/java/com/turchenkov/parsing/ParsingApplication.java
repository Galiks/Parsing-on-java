package com.turchenkov.parsing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@SpringBootApplication
public class ParsingApplication {

    @RequestMapping("/shops")
    public static void main(String[] args) {
        SpringApplication.run(ParsingApplication.class, args);

    }
}
