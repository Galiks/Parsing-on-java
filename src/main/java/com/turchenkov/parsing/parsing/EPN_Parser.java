package com.turchenkov.parsing.parsing;

import com.turchenkov.parsing.domains.EPN;
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

public class EPN_Parser implements ParserInterface {

    private String addressOfSite = "https://epn.bz";
    private String checkWord = "Новый";
    private Pattern patternForDiscount = Pattern.compile("\\d+[.]*\\d*");
    private Pattern patternForLabel = Pattern.compile("[$%€]|руб|(р.)|cent");
    private List<EPN> epns = new ArrayList<>();

    @Override
    public void parsing() throws IOException, InterruptedException {


        int THREADS = 4; // кол-во потоков
        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        List<Callable<Object>> tasks = new ArrayList<>();

        List<String> discounts = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<String> labels = new ArrayList<>();


        Document document = Jsoup.connect("https://epn.bz/ru/cashback/shops/page/1")
                .userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36")
                .post();

        Elements elements = document.select("li.new-pagination__item").select("a[href]");

        int maxPage = Integer.parseInt(elements.get(elements.size() - 2).text());

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

        for (EPN epn : epns) {
            System.out.println(epn);
        }
    }

    private void parsElementsForLetyShops(Pattern patternForLabel, List<String> discounts, List<String> names, List<String> labels, int i) throws IOException {
        Document document;
        Elements elements;
        document = Jsoup.connect("https://epn.bz/ru/cashback/shops/page/" + i)
                .userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36")
                .post();

        elements = document.select("div.shop-list__item");

        for (Element element : elements) {
            if (element.text().contains(checkWord)) {
                if (element.text().length() > 22) {

                    String pageOfShop = element.select("a[href]").attr("href");

                    fillTheListOfEPN(addressOfSite + pageOfShop);
                }
            } else {

                if (element.text().length() > 16) {
                    String pageOfShop = element.select("a[href]").attr("href");

                    fillTheListOfEPN(addressOfSite + pageOfShop);
                }
            }
        }
    }

    private void fillTheListOfEPN(String url) throws IOException {
        Document document = Jsoup.connect(url).post();

        Elements elements = document.select("header.offer-aside__header");

        for (Element element : elements) {

            String image = element.select("img[src]").attr("src");

            String tempElement = element.select("span.offer-aside__info-percent").text();

            Matcher discountMatcher = patternForDiscount.matcher(tempElement);
            if (discountMatcher.find()) {

                Matcher labelMatcher = patternForLabel.matcher(tempElement);

                if (labelMatcher.find()) {

                    epns.add(new EPN(url.substring(33, url.length() - 1), Double.parseDouble(tempElement.substring(discountMatcher.start(), discountMatcher.end())), tempElement.substring(labelMatcher.start(), labelMatcher.end()), url, image));
                }
            }
        }
    }
}
