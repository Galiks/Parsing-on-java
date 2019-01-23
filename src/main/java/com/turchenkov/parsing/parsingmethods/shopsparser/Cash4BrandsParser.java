package com.turchenkov.parsing.parsingmethods.shopsparser;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.turchenkov.parsing.customannotation.Timer;
import com.turchenkov.parsing.domains.shop.Cash4Brands;
import com.turchenkov.parsing.domains.shop.Shop;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Cash4BrandsParser implements ParserInterface {

    private final int startingShops = 8;
    private final int shopsOnPage = 12;
    private final String addressOfSite = "https://cash4brands.ru";
    private final String pageForParsing = "https://cash4brands.ru/cashback";
    private final String patternForMaxShops = "\\d+";
    Pattern patternForDiscount = Pattern.compile("\\d+[.]*\\d*");
    Pattern patternForLabel = Pattern.compile("[$%€]|руб|(р.)|cent|р");
    private Pattern patternForMaxShopsImpl;
    private String patternForPage = "\\/cashback\\/[^\\/]+";
    private Pattern pattern;

    //    @Value("${parsing.useragent}")
    private String userAgent = "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36";

    public Cash4BrandsParser() {
        this.patternForMaxShopsImpl = Pattern.compile(patternForMaxShops);
        this.pattern = Pattern.compile(patternForPage);
    }

    @Override
    @Timer
    public List<Shop> parsing() throws IOException, UnirestException {

        Set<String> secondListOfShops = new HashSet<>();
        List<Shop> pageResult = new LinkedList<>();


        for (String pageOnSite : getShopPages()) {
            Document document;
            try {
                document = Jsoup.connect(pageOnSite).userAgent(userAgent).get();
                String name = getName(document);
                String image = getImage(document);
                Double discount = getDiscount(document);
                String label = getLabel(document);
                Cash4Brands cash4Brands = new Cash4Brands(name, discount, label, pageOnSite, image);
                pageResult.add(cash4Brands);
            } catch (HttpStatusException http) {
                http.printStackTrace();
                System.out.println(http.getStatusCode());
                System.out.println(http.getUrl());
                secondListOfShops.add(pageOnSite);
                continue;
            }


        }

        if (secondListOfShops.size() > 0){
            for (String secondListOfShop : secondListOfShops) {
                Document document;
                try {
                    document = Jsoup.connect(secondListOfShop).userAgent(userAgent).get();
                    String name = getName(document);
                    String image = getImage(document);
                    Double discount = getDiscount(document);
                    String label = getLabel(document);
                    Cash4Brands cash4Brands = new Cash4Brands(name, discount, label, secondListOfShop, image);
                    pageResult.add(cash4Brands);
                } catch (HttpStatusException http){
                    http.printStackTrace();
                    System.out.println(http.getStatusCode());
                    System.out.println(http.getUrl());
                }
            }
        }

        return pageResult;
    }

    private String getLabel(Document document) {
        String discountAndLabel = document.getElementsByClass("color-yellow").text();

        Matcher labelMatcher = patternForLabel.matcher(discountAndLabel);
        if (labelMatcher.find()) {
            return labelMatcher.group();
        }

        return "LABEL";
    }

    private Double getDiscount(Document document) {
        String discountAndLabel = document.getElementsByClass("color-yellow").text();

        Matcher discountMatcher = patternForDiscount.matcher(discountAndLabel);
        if (discountMatcher.find()) {
            return Double.parseDouble(discountMatcher.group());
        }

        return 666666.6666;
    }

    private String getImage(Document document) {
        String result = document.getElementsByClass("logo_shop").first().getElementsByTag("img").attr("src");
        if (result != null) {
            return addressOfSite+result;
        } else {
            return "IMAGE";
        }
    }

    private String getName(Document document) {
        String name = document.getElementsByClass("last").first().text();
        if (name != null) {
            return name;
        } else {
            return "NAME";
        }
    }

    public Set<String> getShopPages() throws IOException, UnirestException {
        Set<String> result = new HashSet<>();
        int maxPage = (getMaxShops() + startingShops) / shopsOnPage;
        for (int i = 1; i <= maxPage; i++) {
            HttpResponse<String> response = Unirest.post("https://cash4brands.ru/cashback/")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("cache-control", "no-cache")
                    .header("Postman-Token", "b6924ce1-29ff-4268-8cec-bea8137d7b6e")
                    .body("namerequest=show_page&undefined=")
                    .asString();

//            System.out.println(response.getBody());

            Matcher matcher = pattern.matcher(response.getBody());
            while (matcher.find()) {
                if (matcher.group().contains("u0441")) {
                    System.out.println("FIND!");
                    result.add("https://cash4brands.ru/cashback/%D1%81redilo-ua/");
                    continue;
                }
                result.add("https://cash4brands.ru" + matcher.group());
            }
        }

        return result;
    }

    private int getMaxShops() throws IOException {
        Document document = Jsoup.connect("https://cash4brands.ru/").userAgent(userAgent).get();
        String pages = document.getElementsByClass("button -transparent -border btn-large").text();
        Matcher matcher = patternForMaxShopsImpl.matcher(pages);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }
        return 1500;
    }
}
