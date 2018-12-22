package com.turchenkov.parsing.parsingmethods.shopsparser;

import com.turchenkov.parsing.domains.shop.LetyShops;
import com.turchenkov.parsing.domains.shop.Shop;
import com.turchenkov.parsing.parsingmethods.timer.MeasuringTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
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

@MeasuringTime
@Component
public class LetyShopsParser implements ParserInterface {


    private final String addressOfSite = "https://letyshops.com";
    private final Pattern patternForDiscount = Pattern.compile("\\d+[.]*\\d*");
    private final Pattern patternForLabel  = Pattern.compile("\\$|%|€|руб|^\\s");
    private final int THREADS = 4;
    private final ExecutorService pool;

    public LetyShopsParser() {

        this.pool = Executors.newFixedThreadPool(THREADS);
    }

    @Override
    public List<Shop> parsing() {
        int maxPage = getMaxPage();

        List<Future<List<Shop>>> futures = new ArrayList<>();
        List<Shop> result;
        try {
            for (int i = 1; i <= maxPage; i++) {
                final int finalI = i;
                futures.add(pool.submit(() -> parsElements(finalI)));
            }
            result = futures.stream().flatMap(getFutureStream()).collect(Collectors.toList());
        } finally {
            pool.shutdown();
        }

        return result;
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

    private List<Shop> parsElements(int i) throws IOException {
        Document document = Jsoup.connect("https://letyshops.com/shops?page=" + i).get();
        Elements items = document.getElementsByClass("b-teaser__inner");

        List<Shop> pageResult = new ArrayList<>();
        for (Element item : items) {
            String title = getName(item);
            String pagesOnTheSite = getPagesOnTheSite(item);
            Double discount = getDiscount(item);
            String label = getLabel(item);
            String img = getImage(item);
            LetyShops letyShops = new LetyShops(title, discount, label, pagesOnTheSite, img);
            pageResult.add(letyShops);
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
        Matcher matcher = patternForDiscount.matcher(discount);
        if (matcher.find()) {
            return Double.parseDouble(discount.substring(matcher.start(), matcher.end()));
        }
        return Double.NaN;
    }

    private int getMaxPage() {
        try {
            Elements elements = Jsoup.connect("https://letyshops.com/shops?page=1")
                    .get()
                    .getElementsByClass("b-pagination__item");
            return Integer.parseInt(elements.get(elements.size() - 2).text());
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }

    }
}
