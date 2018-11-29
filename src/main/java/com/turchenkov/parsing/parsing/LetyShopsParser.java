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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LetyShopsParser implements ParserInterface {

    private String addressOfSite = "https://letyshops.com";
    private Pattern patternForDiscount = Pattern.compile("\\d+[.]*\\d*");
    private Pattern patternForLabel = Pattern.compile("[$%€]|руб|");
    private List<String> discounts = new ArrayList<>();
    private List<String> names = new ArrayList<>();
    private List<String> labels = new ArrayList<>();
    private List<String> pagesOnTheSite = new ArrayList<>();
    private List<String> images = new ArrayList<>();
    private List<LetyShops> letyShops = new ArrayList<>();

    @Override
    public void parsing() throws IOException, InterruptedException {


        int THREADS = 4; // кол-во потоков
        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        List<Callable<Object>> tasks = new ArrayList<>();



        Document document = Jsoup.connect("https://letyshops.com/shops?page=1").get();

        Elements elements = document.getElementsByClass("b-pagination__item");

        int maxPage = Integer.parseInt(elements.get(elements.size() - 2).text());

        try {
            for (int i = 1; i <= maxPage; i++) {
                int finalI = i;
                tasks.add(() -> {
                    parsElementsForLetyShops(finalI);
                    return null;
                });
            }
            List<Future<Object>> invokeAll = pool.invokeAll(tasks);
        } finally {
            pool.shutdown();
        }

        for (int i = 0; i < discounts.size(); i++) {
            Matcher matcher = patternForDiscount.matcher(discounts.get(i));
            if (matcher.find()) {
                letyShops.add(new LetyShops(names.get(i), Double.parseDouble(discounts.get(i).substring(matcher.start(), matcher.end())), labels.get(i), pagesOnTheSite.get(i), images.get(i)));
            }
        }

        for (SiteForParsing letyShop : letyShops) {
            System.out.println(letyShop);
        }
    }

    private void parsElementsForLetyShops(int i) throws IOException {
        Document document;
        Elements elements;
        document = Jsoup.connect("https://letyshops.com/shops?page=" + i).get();

        elements = document.select("div.b-teaser");

        for (Element element : elements) {
            names.add(element.select("div.b-teaser__title").text());
        }

        elements = document.getElementsByClass("b-shop-teaser__cash-value-row");

        for (Element element : elements) {
            discounts.add(element.text());
        }

        elements = document.getElementsByClass("b-shop-teaser__label");

        for (Element element : elements) {
            Matcher matcher = patternForLabel.matcher(element.text());
            if (matcher.find()) {
                labels.add(element.text().substring(matcher.start(), matcher.end()));
            }
        }

        elements = document.select("div.b-teaser__cover");

        for (Element element : elements) {
            images.add(element.select("img[src]").attr("src"));
        }

        elements = document.select("a.b-teaser__inner");

        for (Element element : elements) {
            pagesOnTheSite.add(addressOfSite+element.attr("href"));
        }
    }
}
