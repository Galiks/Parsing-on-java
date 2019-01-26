package com.turchenkov.parsing.parsingmethods;

import com.turchenkov.parsing.domains.shop.Shop;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MegaBonusParser implements ParserInterface {

    private final String patternForShopsPage = ":\"\\\\\\/shop\\\\\\/[^'!@\"#$%^&*()=+№;:?\\\\\\/]+";

    @Override
    public List<Shop> parsing() {
        Pattern pattern = Pattern.compile(patternForShopsPage);

        System.setProperty("webdriver.chrome.driver", "E:/Download/chromedriver_win32/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.navigate().to("https://megabonus.com/feed");

        Set<String> pages = new HashSet<>();
        Matcher matcher1 = pattern.matcher(driver.getPageSource());
        while (matcher1.find()) {
            pages.add("https://megabonus.com/shop" + matcher1.group().substring(9));
        }

        System.out.println("Result size: " + pages.size());
        for (String s : pages) {
            System.out.println(s);
        }

        return null;
    }
}
