package com.turchenkov.parsing.parsingmethods;

import com.turchenkov.parsing.customannotation.Timer;
import com.turchenkov.parsing.domains.shop.LetyShops;
import com.turchenkov.parsing.domains.shop.Shop;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class LetyShopsParser implements ParserInterface {

    private static final Logger log = Logger.getLogger(LetyShopsParser.class);
    private final Pattern patternForDiscount;
    private final Pattern patternForLabel;
    private final ExecutorService pool;
    private final CompletionService<List<Shop>> completionService;
    @Value("${parsing.site.letyshops}")
    private String addressOfSite;


    public LetyShopsParser() {

        int THREADS = Runtime.getRuntime().availableProcessors();
        this.pool = Executors.newFixedThreadPool(THREADS);
        completionService = new ExecutorCompletionService<>(this.pool);
        patternForLabel = Pattern.compile("[$%€]|руб|(р.)|cent");
        patternForDiscount = Pattern.compile("\\d+[.|,]*\\d*");
    }

    @Timer
    @Override
    public List<Shop> parsing() {
        BasicConfigurator.configure();
        int maxPage = getMaxPage();
        List<Future<List<Shop>>> futures = new LinkedList<>();
        List<Shop> result;
        for (int i = 1; i <= maxPage; i++) {
            final int finalI = i;
            futures.add(completionService.submit(() -> parsElements(finalI)));
        }
        result = futures.stream().flatMap(getFutureStream()).collect(Collectors.toList());
        return result;
    }

    @PreDestroy
    private void destroy() {
        pool.shutdown();
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

    private List<Shop> parsElements(int i) throws IOException {
        Document document = Jsoup.connect("https://letyshops.com/shops?page=" + i).get();
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
