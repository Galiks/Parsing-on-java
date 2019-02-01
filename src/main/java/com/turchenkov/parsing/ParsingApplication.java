package com.turchenkov.parsing;


import com.turchenkov.parsing.domains.shop.Cash4Brands;
import com.turchenkov.parsing.domains.shop.Kopikot;
import com.turchenkov.parsing.domains.shop.Shop;
import com.turchenkov.parsing.parsingmethods.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@SpringBootApplication
public class ParsingApplication {

    private static final Logger log = LoggerFactory.getLogger(LetyShopsParser.class);

//https://megabonus.com/shop/compulockscom\/
//    1722

    public static void main(String[] args) throws Exception {
//        log.info("Приложение стартовало");
//        SpringApplication.run(ParsingApplication.class, args);


//        MegaBonusParser megaBonusParser = new MegaBonusParser();
//        for (Shop shop : megaBonusParser.parsing()) {
//            System.out.println(shop);
//        }

//        CashbackoffParser cashbackoffParser = new CashbackoffParser();
//        for (Shop shop : cashbackoffParser.parsing()) {
//            System.out.println(shop);
//        }
//        Long end = System.currentTimeMillis();

        Long start = System.currentTimeMillis();
        KopikotParser kopikotParser = new KopikotParser();
        kopikotParser.parsing();
        Long end = System.currentTimeMillis();
        System.out.println("TIME : " + (end-start)/1000 );
        System.out.println(kopikotParser.page);
    }
}

/**
        * этот кусок кода отвечает за клик. Просто оставлю пока что здесь, чтобы не искать снова
        */
//        for (int i = 0; i < 2; i++) {
//            try {
//                driver.findElement(By.className("see-more")).click();
//            } catch (org.openqa.selenium.ElementNotVisibleException e) {
//                e.printStackTrace();
//                break;
//            }
//        }

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
