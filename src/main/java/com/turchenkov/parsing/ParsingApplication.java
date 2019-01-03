package com.turchenkov.parsing;

import com.turchenkov.parsing.customannotation.Timer;
import com.turchenkov.parsing.domains.shop.LetyShops;
import com.turchenkov.parsing.parsingmethods.shopsparser.Cash4BrandsParser;
import com.turchenkov.parsing.parsingmethods.shopsparser.LetyShopsParser;
import com.turchenkov.parsing.parsingmethods.shopsparser.ParserInterface;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.util.Arrays;

@ComponentScan
@SpringBootApplication
public class ParsingApplication {

    public static void main(String[] args) throws NoSuchMethodException {
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
