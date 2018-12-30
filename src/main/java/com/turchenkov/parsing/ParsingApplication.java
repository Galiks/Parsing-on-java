package com.turchenkov.parsing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;

@ComponentScan
@SpringBootApplication
public class ParsingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParsingApplication.class, args);
    }
}
