package com.turchenkov.parsing.parsingmethods;

import com.turchenkov.parsing.customannotation.Timer;
import com.turchenkov.parsing.domains.shop.MegaBonus;
import com.turchenkov.parsing.domains.shop.Shop;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class WedDriverAndJSoupMegaBonusParser implements ParserInterface {

    private static final Logger log = Logger.getLogger(WedDriverAndJSoupMegaBonusParser.class);

    private final Pattern patternForName = Pattern.compile("Подробнее про кэшбэк в ([\\w\\s\\d\\W]+)");
    private final Pattern patternForDiscount = Pattern.compile("\\d+[.|,]*\\d*");
    private final Pattern patternForLabel = Pattern.compile("[$%€]|руб|(р.)|cent|р|Р");
    private final ExecutorService pool;
    private final String addressForParsing = "https://megabonus.com/feed";
    private final String addressOfSite = "https://megabonus.com";

    public WedDriverAndJSoupMegaBonusParser() {
        int THREADS = Runtime.getRuntime().availableProcessors();
        this.pool = Executors.newFixedThreadPool(THREADS);
    }

    @Timer
    @Override
    public List<Shop> parsing() {
        BasicConfigurator.configure();
        log.info(WedDriverAndJSoupMegaBonusParser.class.getSimpleName() + " is working");
        List<Shop> result = new ArrayList<>();
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
        Document document = Jsoup.parse(listOfElements.getAttribute("outerHTML"));
        Elements elements = document.getElementsByTag("li");
        driver.close();
        List<Future<Shop>> futures = new ArrayList<>();
        for (Element element : elements) {
            futures.add(pool.submit(() -> parseElements(element)));
        }
        for (Future<Shop> future : futures) {
            try {
                result.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                log.error(e);
            }
        }
        return result;
    }

    @PreDestroy
    private void destroy() {
        pool.shutdown();
    }

    private Shop parseElements(Element element) {
        String name = getName(element);
        String image = getImage(element);
        String page = getPage(element);
        String fullDiscount = getFullDiscount(element);
        String label = getLabel(fullDiscount);
        Double discount = getDiscount(fullDiscount);
        if (name != null & image != null & label != null & discount != Double.NaN & page != null) {
            return new MegaBonus(name, discount, label, page, image);
        }
        return null;
    }

    private String getPage(Element element) {
        String page;
        try {
            page = element.getElementsByClass("holder-link pr-shop-card").first().attr("href");
        } catch (NullPointerException e) {
            log.error(e);
            return null;
        }
        return addressOfSite + page;
    }

    private String getLabel(String fullDiscount) {
        Matcher matcher = patternForLabel.matcher(fullDiscount);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    private Double getDiscount(String fullDiscount) {
        String discount = "";
        Matcher matcher = patternForDiscount.matcher(fullDiscount);
        while (matcher.find()) {
            discount = matcher.group();
        }
        try {
            Double trueDiscount = Double.parseDouble(discount.replace(',', '.'));
            return trueDiscount;
        } catch (NumberFormatException e) {
            log.error(e);
            return Double.NaN;
        }
    }

    private String getFullDiscount(Element element) {
        String fullDiscount;
        try {
            fullDiscount = element.select("div.your-percentage > strong").text();
        } catch (NullPointerException e) {
            log.error(e);
            return null;
        }
        return fullDiscount;
    }

    private String getImage(Element element) {
        String image;
        try {
            image = element.getElementsByTag("img").first().attr("src");
        } catch (NullPointerException e) {
            log.error(e);
            return null;
        }
        return image;
    }

    private String getName(Element element) {
        String name;
        try {
            name = element.select("div.activate-hover-block > div.holder-more > a").text();
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
