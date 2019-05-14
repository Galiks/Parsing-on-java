package com.turchenkov.parsing.parsingmethods;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.turchenkov.parsing.customannotation.Timer;
import com.turchenkov.parsing.domains.shop.LetyShops;
import com.turchenkov.parsing.domains.shop.Shop;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class UnirestLetyShopsParser implements ParserInterface {

    private final Logger log = Logger.getLogger(LetyShopsParser.class);
    private Pattern patternForDiscount;
    private Pattern patternForLabel;
    @Value("${parsing.site.letyshops}")
    private String addressOfSite;

    private ExecutorService pool;


    public UnirestLetyShopsParser() {
        patternForLabel = Pattern.compile("[$%€]|руб|(р.)|cent");
        patternForDiscount = Pattern.compile("\\d+[.|,]*\\d*");
        pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Timer
    @Override
    public List<Shop> parsing() {
        List<Shop> result;
        List<Future<List<Shop>>> futures = new ArrayList<>();
        int maxPage = getMaxPage();
        for (int i = 1; i <= maxPage; i++) {
            final int finalI = i;
            futures.add(pool.submit(() -> parsElements(finalI)));
        }
        result = futures.stream().flatMap(getFutureStream()).collect(Collectors.toList());
        return result;
    }

    @PreDestroy
    private void destroy(){

    }

    private Function<Future<List<Shop>>, Stream<? extends Shop>> getFutureStream() {
        return it -> {
            try {
                return it.get().stream();
            } catch (InterruptedException | ExecutionException e) {
                log.error(e);
                return Stream.empty();
            }
        };
    }

    private List<Shop> parsElements(int i) throws IOException, UnirestException {
        HttpResponse<String> response = Unirest.get("https://letyshops.com/shops?page=" + i)
                .header("User-Agent", "PostmanRuntime/7.11.0")
                .header("Accept", "*/*")
                .header("Cache-Control", "no-cache")
                .header("Postman-Token", "03d403a9-0fb6-4893-826c-6f67e5b323e1,e4d3ee09-6a43-4d38-9242-11d564a09454")
                .header("Host", "letyshops.com")
                .header("cookie", "hl=ru_RU; country=RU%3A0; lsvtkn=d7ca64e7165cfd3175dddfa1cc11bf15")
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

    private String getName(Element item) {
        return item.getElementsByClass("b-teaser__title").text();
    }

    private String getImage(Element item) {
        return item.getElementsByClass("b-teaser__cover").first().getElementsByTag("img").attr("src");
    }

    private String getLabel(Element item) {
        String label = item.getElementsByClass("b-shop-teaser__label").text();
        Matcher matcher = patternForLabel.matcher(label);
        if (matcher.find()) {
            return label.substring(matcher.start(), matcher.end());
        }
        return "";
    }

    private String getPagesOnTheSite(Element item) {
        return addressOfSite + item.attr("href");
    }

    private Double getDiscount(Element item) {
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

    private int getMaxPage() {
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
