package com.turchenkov.parsing.parsingmethods;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.turchenkov.parsing.customannotation.Timer;
import com.turchenkov.parsing.domains.shop.Cashbackoff;
import com.turchenkov.parsing.domains.shop.Shop;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

import javax.persistence.Tuple;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CashbackoffParser implements ParserInterface {

    private static final Logger log = Logger.getLogger(CashbackoffParser.class);

    private final String addressOfSite = "https://cashbackoff.ru";

    private final Pattern patternForLabel = Pattern.compile("[$%€]|руб|(р.)|cent");
    private final Pattern patternForDiscount = Pattern.compile("\\d+[.|,]*\\d*");
    private ExecutorService pool;

    public CashbackoffParser() {
        int THREADS = Runtime.getRuntime().availableProcessors();
        this.pool = Executors.newFixedThreadPool(THREADS);
    }

    @Timer
    @Override
    public List<Shop> parsing() {
        BasicConfigurator.configure();
        WebDriver driver = new ChromeDriver();
        driver.get("https://cashbackoff.ru/index_shops_search.php");
        Document document = Jsoup.parse(driver.getPageSource());
        driver.close();
        List<Shop> result = new ArrayList<>();
        List<Future<Shop>> futureList = new ArrayList<>();
        Elements elements = document.getElementsByClass("stores-list-item");
        for (Element element : elements) {
            futureList.add(pool.submit(() -> parseElements(element)));
        }
        for (Future<Shop> shopFuture : futureList) {
            try {
                result.add(shopFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                log.error(e);
            }
        }
        return result;
    }

    private Shop parseElements(Element element) {
        String name = getName(element);
        if (name.equals("М.Видео")){
            System.out.println("HEY!");
        }
        Double discount = getDiscount(getFullDiscount(element));
        String label = getLabel(getFullDiscount(element));
        String shopPage = getShopPage(element);
        String image = getImage(element);
        if (name != null & image != null & discount != Double.NaN & label != null & shopPage != null) {
            return new Cashbackoff(name, discount, label, shopPage, image);
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
            return Double.parseDouble(discount.replace(',', '.'));
        } catch (NumberFormatException e) {
            log.error(e);
            return Double.NaN;
        }
    }

    private String getLabel(String fullDiscount) {
        Matcher matcher = patternForLabel.matcher(fullDiscount);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    private String getImage(Element element) {
        String image = element.getElementsByTag("img").attr("src");
        if (image != null) {
            return addressOfSite + image;
        }
        return null;
    }

    private String getShopPage(Element element) {
        String shopPage = element.getElementsByClass("shoplink").attr("href");
        return addressOfSite + shopPage;
    }


    private String getFullDiscount(Element element) {
        String fullDiscount = element.getElementsByTag("span").text();
        if (fullDiscount != null) {
            return fullDiscount;
        }
        return null;
    }

    private String getName(Element element) {
        String name = element.getElementsByClass("stores-list-item-title").text();
        if (name != null) {
            return name;
        }
        return null;
    }
}
