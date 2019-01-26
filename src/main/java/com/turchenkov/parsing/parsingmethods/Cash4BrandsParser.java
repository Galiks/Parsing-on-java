package com.turchenkov.parsing.parsingmethods.shopsparser;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.turchenkov.parsing.customannotation.Timer;
import com.turchenkov.parsing.domains.shop.Cash4Brands;
import com.turchenkov.parsing.domains.shop.LetyShops;
import com.turchenkov.parsing.domains.shop.Shop;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Cash4BrandsParser implements ParserInterface {

    private final int startingShops = 8;
    private final int shopsOnPage = 12;
    private final String addressOfSite = "https://cash4brands.ru";
    private final String pageForParsing = "https://cash4brands.ru/cashback";
    private final String patternForMaxShops = "\\d+";
    Pattern patternForDiscount = Pattern.compile("\\d+[.]*\\d*");
    Pattern patternForLabel = Pattern.compile("[$%€]|руб|(р.)|cent|р|Р");
    private Pattern patternForMaxShopsImpl;
    private String patternForPage = "\\/cashback\\/[^\\/]+";
    private Pattern pattern;
    private final ExecutorService pool;

    //    @Value("${parsing.useragent}")
    private String userAgent = "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36";

    public Cash4BrandsParser() {
        int THREADS = 4;
        this.pool = Executors.newFixedThreadPool(THREADS);
        this.patternForMaxShopsImpl = Pattern.compile(patternForMaxShops);
        this.pattern = Pattern.compile(patternForPage);
    }

    @Override
    @Timer
    public List<Shop> parsing() throws IOException {


        List<Future<List<Shop>>> futures = new LinkedList<>();
        List<Shop> result;
        final List<String> shopPages = new ArrayList<>(getShopPages());

        for (int i = 0; i < shopPages.size(); i++) {

            final int finalI = i;
            futures.add(pool.submit(() -> parsElements(shopPages.get(finalI))));
        }
        result = futures.stream().flatMap(getFutureStream()).collect(Collectors.toList());

        return result;
    }

    @PreDestroy
    private void destroy(){
        pool.shutdown();
    }

    private List<Shop> parsElements(String shop) throws IOException {
        List<Shop> pageResult = new ArrayList<>();
        try {
            pageResult.add(getShops(shop));
        } catch (HttpStatusException http) {
            http.printStackTrace();
            System.out.println(http.getStatusCode());
            System.out.println(http.getUrl());
            parsElements(shop);
        }

        return pageResult;
    }

    private Shop getShops(String pageOfShop) throws IOException {
        Document document;
        document = Jsoup.connect(pageOfShop).userAgent(userAgent).get();
        String name = getName(document);
        String image = getImage(document);
        Double discount = getDiscount(document);
        String label = getLabel(document);
        if (name != null & image != null & discount != Double.NaN & label != null) {
            return new Cash4Brands(name, discount, label, pageOfShop, image);
        }
        return null;
    }

    private Function<Future<List<Shop>>, Stream<? extends Shop>> getFutureStream() {
        return it -> {
            try {
                return it.get().stream();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return Stream.empty();
            }
        };
    }

    private String getLabel(Document document) {
        String discountAndLabel = document.getElementsByClass("color-yellow").text();

        Matcher labelMatcher = patternForLabel.matcher(discountAndLabel);
        if (labelMatcher.find()) {
            return labelMatcher.group();
        }

        return null;
    }

    private Double getDiscount(Document document) {
        String discountAndLabel = document.getElementsByClass("color-yellow").text();

        Matcher discountMatcher = patternForDiscount.matcher(discountAndLabel);
        if (discountMatcher.find()) {
            return Double.parseDouble(discountMatcher.group());
        }

        return Double.NaN;
    }

    private String getImage(Document document) {
        String result = document.getElementsByClass("logo_shop").first().getElementsByTag("img").attr("src");
        if (result != null) {
            return addressOfSite+result;
        } else {
            return null;
        }
    }

    private String getName(Document document) {
        String name = document.getElementsByClass("last").first().text();
        if (name != null) {
            return name;
        } else {
            return null;
        }
    }

    private Set<String> getShopPages() throws IOException {
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
            } catch (UnirestException e) {
                e.printStackTrace();
            }

//            System.out.println(response.getBody());

            Matcher matcher = pattern.matcher(response.getBody());
            while (matcher.find()) {
                if (matcher.group().contains("u0441")) {
                    System.out.println("FIND!");
                    result.add("https://cash4brands.ru/cashback/%D1%81redilo-ua/");
                    continue;
                }
                result.add(addressOfSite + matcher.group());
            }
        }

        return result;
    }

    private int getMaxShops() throws IOException {
        Document document = Jsoup.connect(addressOfSite).userAgent(userAgent).get();
        String pages = document.getElementsByClass("button -transparent -border btn-large").text();
        Matcher matcher = patternForMaxShopsImpl.matcher(pages);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }
        return 1500;
    }
}
