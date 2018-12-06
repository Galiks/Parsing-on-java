package com.turchenkov.parsing;

import com.turchenkov.parsing.domains.SiteForParsing;
import com.turchenkov.parsing.parsing.ParsingAllSite;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@SpringBootApplication
public class ParsingApplication {

    @RequestMapping("/shops")
    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(ParsingApplication.class, args);
//        ParsingAllSite parsingAllSite = new ParsingAllSite();
//        for (SiteForParsing site : parsingAllSite.parseAllSites()) {
//            System.out.println(site);
//        }
    }
}
