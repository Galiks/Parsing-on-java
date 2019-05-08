package com.turchenkov.parsing.comparing;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.turchenkov.parsing.domains.shop.LetyShops;
import com.turchenkov.parsing.domains.shop.Shop;
import com.turchenkov.parsing.parsingmethods.LetyShopsParser;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


//5 секунд
public class LetyShopParsingUnirest {


    private static final Logger log = Logger.getLogger(LetyShopsParser.class);
    private static Pattern patternForDiscount;
    private static Pattern patternForLabel;
    @Value("${parsing.site.letyshops}")
    private static String addressOfSite;

    public LetyShopParsingUnirest() {
        patternForLabel = Pattern.compile("[$%€]|руб|(р.)|cent");
        patternForDiscount = Pattern.compile("\\d+[.|,]*\\d*");
    }

    public static void main(String[] args) {
        List<Shop> result = new ArrayList<>();
        List<Future<List<Shop>>> futures = new ArrayList<>();
        Long startTime = System.currentTimeMillis();
        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        int maxPage = getMaxPage();
        for (int i = 1; i <= maxPage; i++) {
            final int finalI = i;
            futures.add(pool.submit(() -> parsElements(finalI)));
        }
        result = futures.stream().flatMap(getFutureStream()).collect(Collectors.toList());
        Long endTime = System.currentTimeMillis();
        System.out.println((endTime - startTime) / 1000);
    }

    private static Function<Future<List<Shop>>, Stream<? extends Shop>> getFutureStream() {
        return it -> {
            try {
                return it.get().stream();
            } catch (InterruptedException | ExecutionException e) {
                log.error(e);
                return Stream.empty();
            }
        };
    }

    private static List<Shop> parsElements(int i) throws IOException, UnirestException {
        HttpResponse<String> response = Unirest.get("https://letyshops.com/shops?page=" + i)
                .header("User-Agent", "PostmanRuntime/7.11.0")
                .header("Accept", "*/*")
                .header("Cache-Control", "no-cache")
                .header("Postman-Token", "6e8b5bf8-c735-49cc-89fd-8af5fcbd76ac,d4361b44-415a-4adf-b751-dce1d24183ff")
                .header("Host", "letyshops.com")
                .header("accept-encoding", "gzip, deflate")
                .header("Connection", "keep-alive")
                .header("cache-control", "no-cache")
                .asString();
        Document document = Jsoup.parse(response.getBody());
        Elements items = document.getElementsByClass("b-teaser__inner");
        List<Shop> pageResult = new LinkedList<>();
        for (Element item : items) {
            String name = getName(item);
            String pagesOnTheSite = getPagesOnTheSite(item);
            Double discount = getDiscount(item);
            String label = getLabel(item);
            String image = getImage(item);
            if (name != null & image != null & (discount != Double.NaN & discount != 0) & label != null) {
                LetyShops letyShops = new LetyShops(name, discount, label, pagesOnTheSite, image);
                pageResult.add(letyShops);
            }
        }
        return pageResult;
    }

    private static String getName(Element item) {
        return item.getElementsByClass("b-teaser__title").text();
    }

    private static String getImage(Element item) {
        return item.getElementsByClass("b-teaser__cover").first().getElementsByTag("img").attr("src");
    }

    private static String getLabel(Element item) {
        String label = item.getElementsByClass("b-shop-teaser__label").text();
        Matcher matcher = patternForLabel.matcher(label);
        if (matcher.find()) {
            return label.substring(matcher.start(), matcher.end());
        }
        return "";
    }

    private static String getPagesOnTheSite(Element item) {
        return addressOfSite + item.attr("href");
    }

    private static Double getDiscount(Element item) {
        String discount = item.getElementsByClass("b-shop-teaser__cash-value-row").first().text();
        String trueDiscount = "";
        Matcher matcher = patternForDiscount.matcher(discount);
        while (matcher.find()) {
            trueDiscount = matcher.group();
        }
        try {
            return Double.parseDouble(trueDiscount.replace(',','.'));
        } catch (NumberFormatException e) {
            log.error(e);
            return Double.NaN;
        }
    }

    private static int getMaxPage() {
        try {
            Elements elements = Jsoup.connect("https://letyshops.com/shops?page=1")
                    .get()
                    .getElementsByClass("b-pagination__item");
            return Integer.parseInt(elements.get(elements.size() - 2).text());
        } catch (IOException e) {
            log.error(e);
            return 0;
        }
    }
}
