package com.turchenkov.parsing;

import com.turchenkov.parsing.parsingmethods.shopsparser.LetyShopsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@SpringBootApplication
public class ParsingApplication {

    private static final Logger log = LoggerFactory.getLogger(LetyShopsParser.class);

    public static void main(String[] args) {
        log.info("Приложение стартовало");
        SpringApplication.run(ParsingApplication.class, args);

    }
}
