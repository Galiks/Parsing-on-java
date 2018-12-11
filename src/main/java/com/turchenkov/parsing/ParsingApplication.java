package com.turchenkov.parsing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@SpringBootApplication
@ComponentScan
@Configuration
@ImportResource({"classpath:beans.xml", "file:com.turchenkov.config.beans.xml"})
public class ParsingApplication {

    @RequestMapping("/shops")
    public static void main(String[] args) throws IOException, InterruptedException {
//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("com.turchenkov.config.beans.xml");
        SpringApplication.run(ParsingApplication.class, args);

    }
}
