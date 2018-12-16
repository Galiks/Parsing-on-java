package com.turchenkov.parsing;

import com.turchenkov.parsing.domains.SiteForParsing;
import com.turchenkov.parsing.parsingmethods.ParsingAllSite;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@SpringBootApplication
public class ParsingApplication {

    @RequestMapping("/shops")
    public static void main(String[] args) throws IOException, InterruptedException {
//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("com.turchenkov.config.beans.xml");
        SpringApplication.run(ParsingApplication.class, args);
//        ParsingAllSite parsingAllSite = new ParsingAllSite();
//        for (SiteForParsing site : parsingAllSite.parseAllSites()) {
//            System.out.println(site);
//        }
    }
}
