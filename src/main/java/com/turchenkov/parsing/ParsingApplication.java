package com.turchenkov.parsing;


import com.turchenkov.parsing.parsingmethods.shopsparser.Cash4BrandsParser;
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

//    URL=https://cash4brands.ru/cashback/ikapusta-ua/
//    URL=https://cash4brands.ru/cashback/%5Cu0441redilo-ua/

    public static void main(String[] args) throws Exception {
        log.info("Приложение стартовало");
        SpringApplication.run(ParsingApplication.class, args);

//        Cash4BrandsParser cash4BrandsParser = new Cash4BrandsParser();
//        for (String s : cash4BrandsParser.getShopPages()) {
//            System.out.println(s);
//        }

    }
}
