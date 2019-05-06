package com.turchenkov.parsing;


import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@SpringBootApplication
public class ParsingApplication {

    private static final Logger log = Logger.getLogger(ParsingApplication.class);

    public static void main(String[] args) {
        BasicConfigurator.configure();
        SpringApplication.run(ParsingApplication.class, args);

//        HtmlUnitComparing htmlUnitComparing = new HtmlUnitComparing();
//        htmlUnitComparing.main();

//        MegaBonusParserWithCookie megaBonusParserWithCookie = new MegaBonusParserWithCookie();
//        megaBonusParserWithCookie.parsing();
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
