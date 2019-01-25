package com.turchenkov.parsing;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.turchenkov.parsing.domains.shop.Shop;
import com.turchenkov.parsing.parsingmethods.shopsparser.Cash4BrandsParser;
import com.turchenkov.parsing.parsingmethods.shopsparser.LetyShopsParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

@ComponentScan
@SpringBootApplication
public class ParsingApplication {

    private static final Logger log = LoggerFactory.getLogger(LetyShopsParser.class);

//    URL=https://cash4brands.ru/cashback/ikapusta-ua/
//    URL=https://cash4brands.ru/cashback/%5Cu0441redilo-ua/
//    1722

    public static void main(String[] args) throws Exception {
//        log.info("Приложение стартовало");
//        SpringApplication.run(ParsingApplication.class, args);

        System.setProperty("webdriver.chrome.driver", "E:/Download/chromedriver_win32/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.navigate().to("https://megabonus.com/feed");

        for (int i = 0; i < 2; i++) {
            try {
                driver.findElement(By.className("see-more")).click();
            } catch (org.openqa.selenium.ElementNotVisibleException e) {
                e.printStackTrace();
                break;
            }
        }

        List<WebElement> shops = driver.findElements(By.className(""));
        System.out.println(shops.size());

        for (WebElement shop : shops) {
            System.out.println(shop.getTagName());
        }

        System.out.println("I'm Alive!");
//        List<WebElement> shops1 = driver.findElements(By.className("holder-link pr-shop-card"));
//        System.out.println(shops1.size());

    }
}
