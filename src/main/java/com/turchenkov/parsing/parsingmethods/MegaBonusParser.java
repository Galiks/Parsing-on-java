package com.turchenkov.parsing.parsingmethods;

import com.turchenkov.parsing.customannotation.Timer;
import com.turchenkov.parsing.domains.shop.MegaBonus;
import com.turchenkov.parsing.domains.shop.Shop;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MegaBonusParser implements ParserInterface {

    private final String patternForShopsPage = ":\"\\\\\\/shop\\\\\\/[^'!@\"#$%^&*()=+№;:?\\\\\\/]+";
    private final Pattern pattern = Pattern.compile(patternForShopsPage);
    private final Pattern patternForDiscount = Pattern.compile("\\d+[.]*\\d*");
    private final Pattern patternForLabel = Pattern.compile("[$%€]|руб|(р.)|cent|р|Р");
    private final ExecutorService pool;

    public MegaBonusParser() {
        int THREADS = 4;
        this.pool = Executors.newFixedThreadPool(THREADS);
    }

    @Timer
    @Override
    public List<Shop> parsing() {

        System.setProperty("webdriver.chrome.driver", "E:/Download/chromedriver_win32/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.navigate().to("https://megabonus.com/feed");
        List<Shop> result = new ArrayList<>();
        Set<String> pages = new HashSet<>();
        Matcher matcher1 = pattern.matcher(driver.getPageSource());
        while (matcher1.find()) {
            pages.add("https://megabonus.com/shop" + matcher1.group().substring(9));
        }

        driver.close();

        List<Future<Shop>> futures = new ArrayList<>();

        for (String page : pages) {
            futures.add(pool.submit(()->parseElements(page)));
        }

        for (Future<Shop> future : futures) {
            try {
                result.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        return result;

    }

    private Shop parseElements(String page) {
        WebDriver driver = new ChromeDriver();
        driver.navigate().to(page);
        Document document = Jsoup.parse(driver.getPageSource());
        driver.close();
        String name = getName(document);
        String image = getImage(document);
        String fullDiscount = getFullDiscount(document);
        String label = getLabel(fullDiscount);
        Double discount = getDiscount(fullDiscount);
        if (name != null & image != null & label != null & (discount != Double.NaN & discount != 0)){
            return new MegaBonus(name, discount, label, page, image);
        }
        return null;
    }

    private String getLabel(String fullDiscount){
        Matcher matcher = patternForLabel.matcher(fullDiscount);
        if (matcher.find()){
            return matcher.group();
        }
        return null;
    }

    private Double getDiscount(String fullDiscount){
        Matcher matcher = patternForDiscount.matcher(fullDiscount);
        if (matcher.find()){
            return Double.parseDouble(matcher.group());
        }
        return Double.NaN;
    }

    private String getFullDiscount(Document document) {
        return document.select("#cashback-categories > div.categories > div:nth-child(1) > p.cashback-price").text();
    }

    private String getImage(Document document) {
        String image = document.select("#cashback-categories > div.shop-img > img").first().attr("src");
        if (image != null){
            return image;
        }
        return null;
    }

    private String getName(Document document) {
        String name = document.select("#main > div.container.shop-card > div.shop-content > div.breadcrumbs > div > ul > li:nth-child(2) > span > span").text();
        if (name != null){
            return name;
        }
        return null;
    }

    @PreDestroy
    private void destroy() {
        pool.shutdown();
    }
}
