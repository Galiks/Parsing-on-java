package com.turchenkov.parsing;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.turchenkov.parsing.domains.shop.Shop;
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
//        for (Shop shop : cash4BrandsParser.parsing()) {
//            System.out.println(shop);
//        }
//        for (Shop shop : cash4BrandsParser.parsing()) {
//            System.out.println(shop);
//        }

//        LetyShopsParser letyShopsParser = new LetyShopsParser();
//        for (Shop shop : letyShopsParser.parsing()) {
//            System.out.println(shop);
//        }

    }
}
