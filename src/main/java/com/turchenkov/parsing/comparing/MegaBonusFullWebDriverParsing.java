package com.turchenkov.parsing.comparing;

import com.turchenkov.parsing.customannotation.ConsoleTimer;
import com.turchenkov.parsing.domains.shop.MegaBonus;
import com.turchenkov.parsing.domains.shop.Shop;
import com.turchenkov.parsing.parsingmethods.MegaBonusParser;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//67 секунд
public class MegaBonusFullWebDriverParsing {
    private static final Logger log = Logger.getLogger(MegaBonusParser.class);

    private static final Pattern patternForName = Pattern.compile("Подробнее про кэшбэк в ([\\w\\s\\d\\W]+)");
    private static final Pattern patternForDiscount = Pattern.compile("\\d+[.|,]*\\d*");
    private static final Pattern patternForLabel = Pattern.compile("[$%€]|руб|(р.)|cent|р|Р");

    private static final String addressForParsing = "https://megabonus.com/feed";
    private static final String addressOfSite = "https://megabonus.com";

    public MegaBonusFullWebDriverParsing() {

    }

    public static void main(String[] args) {
        parsing();
    }

    @ConsoleTimer
    public static List<Shop> parsing() {
        BasicConfigurator.configure();
        try {
            System.setProperty("webdriver.chrome.driver", "chromedriver_win32/chromedriver.exe");
        } catch (Exception e) {
            log.error(e);
        }
        log.info(MegaBonusParser.class.getSimpleName() + " is working");
        List<Shop> result = new ArrayList<>();
        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<Shop>> futures = new ArrayList<>();
        Long startTime = System.currentTimeMillis();
        WebDriver driver = new ChromeDriver();
        driver.navigate().to(addressForParsing);
        WebElement button = driver.findElement(By.className("see-more"));
        while (button.isDisplayed()) {
            try {
                button.click();
                button = driver.findElement(By.className("see-more"));
            } catch (Exception e) {
                log.error(e);
            }
        }
        WebElement listOfElements = driver.findElement(By.className("cacheback-block-list"));
        List<WebElement> elements = listOfElements.findElements(By.tagName("li"));
        driver.close();
        for (WebElement element : elements) {
            futures.add(pool.submit(() -> parseElements(element)));
        }
        for (Future<Shop> future : futures) {
            try {
                result.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                log.error(e);
            }
        }
        Long endTime = System.currentTimeMillis();
        System.out.println((endTime - startTime) / 1000);
        return result;
    }

    private static Shop parseElements(WebElement element) {
        String name = getName(element);
        String fullDiscount = getFullDiscount(element);
        Double discount = getDiscount(fullDiscount);
        String image = getImage(element);
        String label = getLabel(fullDiscount);
        String url = getUrl(element);
        return new MegaBonus(name, discount, label, url, image);
    }

    private static String getUrl(WebElement element) {
        String url;
        try {
            url = element.findElement(By.cssSelector("\"div.holder-img > a\"")).getAttribute("href");
        } catch (Exception e) {
            log.error(e);
            return null;
        }
        return url;
    }

    private static String getFullDiscount(WebElement element) {
        String fullDiscount;
        try {
            fullDiscount = element.findElement(By.cssSelector("\"div.your-percentage > strong\"")).getText();
        } catch (Exception e) {
            log.error(e);
            return null;
        }
        return fullDiscount;
    }

    private static String getLabel(String fullDiscount) {
        if (fullDiscount == null) {
            return null;
        }
        Matcher matcher = patternForLabel.matcher(fullDiscount);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    private static Double getDiscount(String fullDiscount) {
        if (fullDiscount == null) {
            return Double.NaN;
        }
        String discount = "";
        Matcher matcher = patternForDiscount.matcher(fullDiscount);
        if (matcher.find()) {
            discount = matcher.group();
        }
        try {
            return Double.parseDouble(discount);
        } catch (NumberFormatException e) {
            log.error(e);
            return Double.NaN;
        }
    }

    private static String getImage(WebElement element) {
        String image;
        try {
            image = element.findElement(By.tagName("img")).getAttribute("src");
        } catch (Exception e) {
            log.error(e);
            return null;
        }
        return image;
    }

    private static String getName(WebElement element) {
        String name;
        try {
            name = element.findElement(By.className("holder-more")).findElement(By.tagName("a")).getAttribute("innerHTML");
        } catch (Exception e) {
            log.error(e);
            return null;
        }
        Matcher matcher = patternForName.matcher(name);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
