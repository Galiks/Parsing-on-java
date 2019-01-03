package com.turchenkov.parsing;

import com.turchenkov.parsing.domains.shop.LetyShops;
import com.turchenkov.parsing.parsingmethods.shopsparser.Cash4BrandsParser;
import com.turchenkov.parsing.parsingmethods.shopsparser.LetyShopsParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;

@ComponentScan
@SpringBootApplication
public class ParsingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParsingApplication.class, args);
//        Cash4BrandsParser cash4BrandsParser = new Cash4BrandsParser();
//        try {
//            cash4BrandsParser.parsing();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
