package com.turchenkov.parsing.parsing;

import com.turchenkov.parsing.domains.LetyShops;
import com.turchenkov.parsing.domains.SiteForParsing;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

public class LetyShopsParser implements ParserInterface {


    private final String addressOfSite;
    private final Pattern patternForDiscount;
    private final Pattern patternForLabel;
    private final ExecutorService pool;

    public LetyShopsParser() {
//        вынести в константы. в файл пропертей. затягивать через @value
        this.addressOfSite = "https://letyshops.com";
        this.patternForDiscount = Pattern.compile("\\d+[.]*\\d*");
        this.patternForLabel = Pattern.compile("[$%€]|руб|");
        int THREADS = 4; // кол-во потоков
//        _____________________________________

        this.pool = Executors.newFixedThreadPool(THREADS);
    }

    @Override
    public List<SiteForParsing> parsing() {
        int maxPage = getMaxPage();

        List<Future<List<LetyShops>>> futures = new ArrayList<>();
        List<SiteForParsing> result;
        try {
            for (int i = 1; i <= maxPage; i++) {
                final int finalI = i;
                futures.add(pool.submit(() -> parsElementsForLetyShops(finalI)));
            }
            result = futures.stream().flatMap(getFutureStream()).collect(Collectors.toList());
        } finally {
            pool.shutdown();
        }

        return result;
    }

    private Function<Future<List<LetyShops>>, Stream<? extends LetyShops>> getFutureStream() {
        return it -> {
            try {
                return it.get().stream();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return Stream.empty();
            }
        };
    }

    private List<LetyShops> parsElementsForLetyShops(int i) throws IOException {
        Document document = Jsoup.connect("https://letyshops.com/shops?page=" + i).get();
        Elements items = document.getElementsByClass("b-teaser__inner");

        List<LetyShops> pageResult = new ArrayList<>();
        for (Element item : items) {
            String title = getTitle(item);
            String pagesOnTheSite = getPagesOnTheSite(item);
            Double discount = getDiscount(item);
            String label = getLabel(item);
            String img = getImage(item);
            LetyShops letyShops = new LetyShops(title, discount, label, pagesOnTheSite, img);
            pageResult.add(letyShops);
        }
        return pageResult;
    }

    private String getTitle(Element item) {
        return item.getElementsByClass("b-teaser__title").text();
    }

    private String getImage(Element item) {
        return item.getElementsByClass("b-teaser__cover").first().getElementsByTag("img").attr("src");
    }

    private String getLabel(Element item) {
        String label = item.getElementsByClass("b-shop-teaser__label").first().text();
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
