package com.turchenkov.parsing;


import com.turchenkov.parsing.domains.shop.Shop;
import com.turchenkov.parsing.parsingmethods.UnirestLetyShopsParser;
import lombok.var;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@ComponentScan
@SpringBootApplication
public class ParsingApplication {

    private static final Logger log = Logger.getLogger(ParsingApplication.class);

    public static void main(String[] args) {
        BasicConfigurator.configure();
        try {
            System.setProperty("webdriver.chrome.driver", "chromedriver_win32/chromedriver.exe");
        } catch (Exception e) {
            log.error(e);
        }
        SpringApplication.run(ParsingApplication.class, args);
    }
}

/**
 * прикольный метод для поиска дублей
 */
//    public static Set<String> findDuplicates(List<String> listContainingDuplicates)
//    {
//        final Set<String> setToReturn = new HashSet();
//        final Set<String> set1 = new HashSet();
//
//        for (String yourInt : listContainingDuplicates)
//        {
//            if (!set1.add(yourInt))
//            {
//                setToReturn.add(yourInt);
//            }
//        }
//        return setToReturn;
//    }
