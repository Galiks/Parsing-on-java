package com.turchenkov.parsing.parsingmethods;

import com.jayway.jsonpath.JsonPath;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.turchenkov.parsing.customannotation.Timer;
import com.turchenkov.parsing.domains.shop.Cash4Brands;
import com.turchenkov.parsing.domains.shop.Shop;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Cash4BrandsParser implements ParserInterface {

    private final int startingShops = 8;
    private final int shopsOnPage = 12;
    private final String addressOfSite = "https://cash4brands.ru";
    private final ExecutorService pool;
    Pattern patternForDiscount = Pattern.compile("\\d+[.]*\\d*");
    Pattern patternForLabel = Pattern.compile("[$%€]|руб|(р.)|cent|р|Р");
    private Pattern patternForMaxShops = Pattern.compile("\\d+");
    //    @Value("${parsing.useragent}")
    private String userAgent = "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36";

    public Cash4BrandsParser() {
        int THREADS = 4;
        this.pool = Executors.newFixedThreadPool(THREADS);
    }

    @Override
    @Timer
    public List<Shop> parsing() {


        List<Future<List<Object>>> futures = new LinkedList<>();
        List<Shop> result = new ArrayList<>();
        List<List<Object>> html = new ArrayList<>();

        try {
            for (String s : getShopsJson()) {
                futures.add(pool.submit(() -> JsonPath.read(s, "$..shops[*]")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Future<List<Object>> future : futures) {
            try {
                html.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        for (List<Object> list : html) {
            for (Object object : list) {
                result.add(parseElements(object));
            }
        }

        return result;
    }

    private Shop parseElements(Object o) {
        Element element = Jsoup.parse(o.toString()).getElementsByClass("__item_wrap").first();
        String page = getPage(element);
        String image = getImage(element);
        String name = getName(element);
        String fullDiscount = element.getElementsByClass("ufp_bonus").text();
        if (fullDiscount == null){
            return null;
        }
        String label = getLabel(fullDiscount);
        Double discount = getDiscount(fullDiscount);
        if (name != null & image != null & (discount != Double.NaN & discount != 0) & label != null & page != null) {
            return new Cash4Brands(name, discount, label, page, image);
        }
        return null;
    }

    private String getName(Element element) {
        return element.getElementsByTag("h3").text();
    }

    private String getImage(Element element) {
        return addressOfSite + element.getElementsByClass("--logo").first().getElementsByTag("img").attr("src");
    }

    private String getPage(Element element) {
        return addressOfSite + element.attr("href");
    }

    @PreDestroy
    private void destroy() {
        pool.shutdown();
    }

    private String getLabel(String fullDiscount){
        Matcher labelMatcher = patternForLabel.matcher(fullDiscount);
        if (labelMatcher.find()) {
            return labelMatcher.group();
        }
        return null;
    }

    private Double getDiscount(String fullDiscount) {
        Matcher discountMatcher = patternForDiscount.matcher(fullDiscount);
        String discount = "";
        while (discountMatcher.find()){
            discount = discountMatcher.group();
        }
        try {
            return Double.valueOf(discount);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        return Double.NaN;
    }

    private Set<String> getShopsJson() throws IOException {
        Set<String> result = new HashSet<>();
        int maxPage = (getMaxShops() + startingShops) / shopsOnPage;
        for (int i = 1; i <= maxPage; i++) {
            HttpResponse<String> response = null;
            try {
                response = Unirest.post("https://cash4brands.ru/cashback/")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("cache-control", "no-cache")
                        .header("Postman-Token", "9774dfc4-97d7-433f-93b1-e37c4a6e1fe3")
                        .body("namerequest=show_page&undefined=")
                        .asString();
                result.add(response.getBody());
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private int getMaxShops() throws IOException {
        Document document = Jsoup.connect(addressOfSite).userAgent(userAgent).get();
        String pages = document.getElementsByClass("button -transparent -border btn-large").text();
        Matcher matcher = patternForMaxShops.matcher(pages);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }
        return 1500;
    }
}
