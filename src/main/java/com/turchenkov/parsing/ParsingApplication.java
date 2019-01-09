package com.turchenkov.parsing;

import com.turchenkov.parsing.customannotation.Timer;
import com.turchenkov.parsing.domains.shop.LetyShops;
import com.turchenkov.parsing.domains.shop.Shop;
import com.turchenkov.parsing.parsingmethods.shopsparser.Cash4BrandsParser;
import com.turchenkov.parsing.parsingmethods.shopsparser.LetyShopsParser;
import com.turchenkov.parsing.parsingmethods.shopsparser.ParserInterface;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.util.*;

@ComponentScan
@SpringBootApplication
public class ParsingApplication {

    public static void main(String[] args) throws NoSuchMethodException {
        SpringApplication.run(ParsingApplication.class, args);

//        Map<Integer, Integer> map = new TreeMap<>();
//
//        try {
//            map.put(null, 3);
//        } catch (NullPointerException e) {
//
//        } finally {
//            map.put(2,null);
//            map.put(1, 2);
//            map.put(3, 4);
//
//            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
//                System.out.println(entry.getKey() + " " + entry.getValue());
//            }
//        }
//
//        Map map1 = new HashMap();


    }
}
