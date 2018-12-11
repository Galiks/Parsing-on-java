package com.turchenkov.parsing.parsingmethods;

import com.turchenkov.parsing.domains.EPN;
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

public class EPN_Parser implements ParserInterface {

    //избавить от констант, перенеся их в application.properties
    private final String addressOfSite = "https://epn.bz";
    private final String startURL = "https://epn.bz/ru/cashback/shops/page/1";
    private final String checkWord = "Новый";
    private final String userAgent = "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36";
    private final String parsingURL = "https://epn.bz/ru/cashback/shops/page/";
    private final Pattern patternForDiscount = Pattern.compile("\\d+[.]*\\d*");
    private final Pattern patternForLabel = Pattern.compile("[$%€]|руб|(р.)|cent");
    private final int THREADS = 6;
    private final ExecutorService pool;

    public EPN_Parser() {
        this.pool = Executors.newFixedThreadPool(THREADS);
    }

    @Override
    public List<SiteForParsing> parsing() {
        int maxPage = getMaxPage();

        List<Future<List<SiteForParsing>>> futures = new ArrayList<>();
        List<SiteForParsing> result;
        try {
            for (int i = 1; i <= maxPage; i++) {
                final int finalI = i;
                futures.add(pool.submit(()->parsElements(finalI)));
            }
            result = futures.stream().flatMap(getFutureStream()).collect(Collectors.toList());
        } finally {
            pool.shutdown();
        }

        return result;
    }

    private Function<Future<List<SiteForParsing>>, Stream<? extends SiteForParsing>> getFutureStream() {
        return it -> {
            try {
                return it.get().stream();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return Stream.empty();
            }
        };
    }

    private int getMaxPage() {
        try {
            Elements elements = Jsoup.connect(startURL).userAgent(userAgent).post().select("li.new-pagination__item").select("a[href]");
            return Integer.parseInt(elements.get(elements.size() - 2).text());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //почему один? потому что одна страница точно есть
        return 1;
    }

    private List<SiteForParsing> parsElements(int i) throws IOException {

        Document document = Jsoup.connect(parsingURL + i)
                .userAgent(userAgent)
                .post();

        Elements elements = document.select("div.shop-list__item");

        List<SiteForParsing> epnList = new ArrayList<>();

        for (Element element : elements) {
            if (element.text().contains(checkWord)) {
                //избавиться от магического числа 22 - количество символов, после которых начинается дисконт, если есть слово "Новый"
                if (element.text().length() > 22) {

                    String pageOfShop = element.select("a[href]").attr("href");
                    String fullURL = addressOfSite + pageOfShop;
                    String name = getName(fullURL);
                    Double discount = getDiscount(fullURL);
                    String image = getImage(fullURL);
                    String label = getLabel(fullURL);
                    epnList.add(new EPN(name,discount,label, fullURL, image));
                }
            } else {
                //избавиться от магического числа 16 - количество символов, после которых начинается дисконт, если нет слова "Новый"
                if (element.text().length() > 16) {
                    String pageOfShop = element.select("a[href]").attr("href");
                    String fullURL = addressOfSite + pageOfShop;
                    String name = getName(fullURL);
                    Double discount = getDiscount(fullURL);
                    String image = getImage(fullURL);
                    String label = getLabel(fullURL);
                    epnList.add(new EPN(name,discount,label, fullURL, image));
                }
            }
        }

        return epnList;
    }

    private String getLabel(String url) {
        try {
            String element = Jsoup.connect(url).userAgent(userAgent).post().select("header.offer-aside__header").select("span.offer-aside__info-percent").first().text();
            Matcher labelMatcher = patternForLabel.matcher(element);
            if (labelMatcher.find()){
                return element.substring(labelMatcher.start(), labelMatcher.end());
            }

            return "";
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    private String getImage(String url) {
        try {
            return Jsoup.connect(url).userAgent(userAgent).post().select("div.offer-aside__logo-wrapper").select("img[src]").attr("src");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    private Double getDiscount(String url) {
        try {
            String element = Jsoup.connect(url).userAgent(userAgent).post().select("header.offer-aside__header").select("span.offer-aside__info-percent").first().text();
            Matcher discountMatcher = patternForDiscount.matcher(element);
            if (discountMatcher.find()){
                return Double.parseDouble(element.substring(discountMatcher.start(), discountMatcher.end()));
            }
            return Double.NaN;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Double.NaN;
    }

    //избавиться от магического числа 33 - количество символов, после которых начинается имя сайта, так как на сайте нет имени, а только картинка
    private String getName(String url) {
        return url.substring(33, url.length() - 1);
    }

}
