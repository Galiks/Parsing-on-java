package com.turchenkov.parsing.parsingmethods;

import com.turchenkov.parsing.customannotation.Timer;
import com.turchenkov.parsing.domains.shop.EPN;
import com.turchenkov.parsing.domains.shop.Shop;
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
public class EPNParser implements ParserInterface{

    //избавить от констант, перенеся их в application.properties

    @Value("${parsing.site.epn}")
    private String addressOfSite;

    @Value("${parsing.useragent}")
    private String userAgent;

    @Value("${parsing.site.epn.checkword}")
    private String checkWord;

    @Value("${parsing.site.epn.startURL}")
    private String startURL;

    @Value("${parsing.site.epn.parsingURL}")
    private String parsingURL;

    private final Pattern patternForDiscount = Pattern.compile("\\d+[.]*\\d*");
    private final Pattern patternForLabel = Pattern.compile("[$%€]|руб|(р.)|cent");
    private final int THREADS = 4;
    private final ExecutorService pool;

    public EPNParser() {
        this.pool = Executors.newFixedThreadPool(THREADS);
    }

    @Timer
    @Override
    public List<Shop> parsing() {
        int maxPage = getMaxPage();

        List<Future<List<Shop>>> futures = new LinkedList<>();
        List<Shop> result;
        try {
            for (int i = 1; i <= maxPage; i++) {
                final int finalI = i;
                futures.add(pool.submit(()->parsElements(finalI)));
            }
            result = futures.stream().flatMap(getFutureStream()).collect(Collectors.toList());
        } finally {

        }

        return result;
    }

    @PreDestroy
    private void destroy(){
        pool.shutdown();
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

    private List<Shop> parsElements(int i) throws IOException {

        Document document = Jsoup.connect(parsingURL + i)
                .userAgent(userAgent)
                .post();

        Elements elements = document.select("div.shop-list__item");

        List<Shop> epnList = new LinkedList<>();

        for (Element element : elements) {
            if (element.text().contains(checkWord)) {
                //избавиться от магического числа 22 - количество символов, после которых начинается дисконт, если есть слово "Новый"
                if (element.text().length() > 22) {

                    getElements(epnList, element);
                }
            } else {
                //избавиться от магического числа 16 - количество символов, после которых начинается дисконт, если нет слова "Новый"
                if (element.text().length() > 16) {
                    getElements(epnList, element);
                }
            }
        }

        return epnList;
    }

    private void getElements(List<Shop> epnList, Element element) {
        String pageOfShop = element.select("a[href]").attr("href");
        String fullURL = addressOfSite + pageOfShop;
        String name = getName(fullURL);
        Double discount = getDiscount(fullURL);
        String image = getImage(fullURL);
        String label = getLabel(fullURL);
        if (name != null & image != null & (discount != Double.NaN & discount != 0) & label != null) {
            epnList.add(new EPN(name, discount, label, fullURL, image));
        }
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
        return url.toUpperCase().charAt(33) + url.substring(34, url.length() - 1);
    }

}
