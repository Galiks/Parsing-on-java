package com.turchenkov.parsing;


import com.turchenkov.parsing.parsingmethods.LetyShopsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@SpringBootApplication
public class ParsingApplication {

    private static final Logger log = LoggerFactory.getLogger(LetyShopsParser.class);

    public static void main(String[] args) throws Exception {
        log.info("Приложение стартовало");
        SpringApplication.run(ParsingApplication.class, args);
    }
}

/**
 * этот кусок кода отвечает за клик. Просто оставлю пока что здесь, чтобы не искать снова
 * <p>
 * прикольный метод для поиска дублей
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
