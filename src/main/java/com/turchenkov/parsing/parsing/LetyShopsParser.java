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

    @Override
    public void parsing() throws IOException, InterruptedException {
        Pattern patternForDiscount = Pattern.compile("\\d+[.]*\\d*");
        Pattern patternForLabel = Pattern.compile("[$%€]|руб");

        int THREADS = 4; // кол-во потоков
        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        List<Callable<Object>> tasks = new ArrayList<>();

        List<String> discounts = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        List<SiteForParsing> letyShops = new ArrayList<>();

        Document document = Jsoup.connect("https://letyshops.com/shops?page=1").get();

        Elements elements = document.getElementsByClass("b-pagination__item");

        int maxPage = Integer.parseInt(elements.get(elements.size() - 2).text());

        long start = System.currentTimeMillis();
        try {
            for (int i = 1; i <= maxPage; i++) {
                int finalI = i;
                tasks.add(() -> {
                    parsElementsForLetyShops(patternForLabel, discounts, names, labels, finalI);
                    return null;
                });
            }
            List<Future<Object>> invokeAll = pool.invokeAll(tasks);
        } finally {
            pool.shutdown();
        }
        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;

        System.out.println(timeConsumedMillis);

        System.out.println(labels.size() + " : " + discounts.size() + " : " + names.size());


        for (int i = 0; i < discounts.size(); i++) {
            Matcher matcher = patternForDiscount.matcher(discounts.get(i));
            if (matcher.find()) {
                letyShops.add(new LetyShops(names.get(i), Double.parseDouble(discounts.get(i).substring(matcher.start(), matcher.end())), labels.get(i)));
            }
        }

        for (SiteForParsing letyShop : letyShops) {
            System.out.println(letyShop);
        }
    }

    private void parsElementsForLetyShops(Pattern patternForLabel, List<String> discounts, List<String> names, List<String> labels, int i) throws IOException {
        Document document;
        Elements elements;
        document = Jsoup.connect("https://letyshops.com/shops?page=" + i).get();

        elements = document.getElementsByClass("b-teaser__title");

        for (Element element : elements) {
            names.add(element.text());
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

        //System.out.println(labels.size() + " : " + discounts.size() + " : " + names.size());
    }
}
