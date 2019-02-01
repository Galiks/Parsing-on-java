package com.turchenkov.parsing.parsingmethods;

import com.turchenkov.parsing.domains.shop.MegaBonus;
import com.turchenkov.parsing.domains.shop.Shop;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MegaBonusParser implements ParserInterface {

    private final String patternForShopsPage = ":\"\\\\\\/shop\\\\\\/[^'!@\"#$%^&*()=+№;:?\\\\\\/]+";
    private final Pattern pattern = Pattern.compile(patternForShopsPage);
    private final String userAgent = "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36";
    private final Pattern patternForTrueShopPage = Pattern.compile("\"(.*?)\"");
    private final Pattern patternForFunctionOfUrl = Pattern.compile("url \\+=(.*?);");
    private final Pattern patternForInitialParameterOfUrl = Pattern.compile("\\(\"(.*?)\"\\)");
    private final Pattern patternForDiscount = Pattern.compile("\\d+[.]*\\d*");
    private final Pattern patternForLabel = Pattern.compile("[$%€]|руб|(р.)|cent|р|Р");
    private final ExecutorService pool;

    public MegaBonusParser() {
        int THREADS = 4;
        this.pool = Executors.newFixedThreadPool(THREADS);
    }

    @Override
    public List<Shop> parsing() {

        System.setProperty("webdriver.chrome.driver", "E:/Download/chromedriver_win32/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.navigate().to("https://megabonus.com/feed");


        Set<String> pages = new HashSet<>();
        Matcher matcher1 = pattern.matcher(driver.getPageSource());
        while (matcher1.find()) {
            pages.add("https://megabonus.com/shop" + matcher1.group().substring(9));
        }


        System.out.println("Block 1 started");
        Long start = System.currentTimeMillis();

        List<Future<String>> futuresPages = new ArrayList<>();
        List<String> shopPages = new ArrayList<>();
        for (String page : pages) {
            futuresPages.add(pool.submit(() -> getTruePages(page)));
        }

        for (Future<String> futuresPage : futuresPages) {
            try {
                shopPages.add(futuresPage.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        Long end = System.currentTimeMillis();
        System.out.println("Block 1 finished for " + (end - start) + " second");

        List<Shop> result = new ArrayList<>();

        System.out.println("Block 2 started");
        start = System.currentTimeMillis();
        for (String shopPage : shopPages) {
            Shop shop = parseElements(shopPage);
            if (shop != null) {
                result.add(shop);
            }
        }
        end = System.currentTimeMillis();
        System.out.println("Block 2 finished for " + (end - start) + " second");

//        List<Future<Shop>> futures = new LinkedList<>();
//        List<Shop> result = new ArrayList<>();
//
//        for (int i = 0; i < shopPages.size(); i++) {
//
//            final int finalI = i;
//            futures.add(pool.submit(() -> parseElements(shopPages.get(finalI))));
//        }
//
//        for (Future<Shop> future : futures) {
//            try {
//                result.add(future.get());
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//        }

        return result;

    }

    private String getTruePages(String dirtyPage) throws IOException {

        StringBuilder resultURL = new StringBuilder();


        Document document = Jsoup.connect(dirtyPage).get();
        String document1 = Jsoup.connect(document.location()).userAgent(userAgent).get().outerHtml();


        Matcher matcher2 = patternForInitialParameterOfUrl.matcher(document1);
        if (matcher2.find()) {
            resultURL.append(matcher2.group(1));
        }

        Matcher matcher = patternForFunctionOfUrl.matcher(document1);

        if (matcher.find()) {
            Matcher matcher1 = patternForTrueShopPage.matcher(matcher.group(1));
            while (matcher1.find()) {
                resultURL.append(matcher1.group(1));
            }
        }

        return resultURL.toString();
    }

    @PreDestroy
    private void destroy() {
        pool.shutdown();
    }

    private Shop parseElements(String shop) {

//        Set set = new TreeSet();

        Document document;
        try {
            document = Jsoup.connect(shop).get();
        } catch (HttpStatusException httpStatusException) {
            httpStatusException.getStatusCode();
            httpStatusException.getUrl();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        String name = getName(document);
        String image = getImage(document);
        Double discount = getDiscount(document);
        String label = getLabel(document);
        if (name != null & image != null & discount != Double.NaN & label != null) {
            return new MegaBonus(name, discount, label, shop, image);
        }
        return null;
    }

    private String getImage(Document document) {
        String image = document.getElementsByClass("shop-img").first().getElementsByTag("img").attr("src");
        if (image != null) {
            return image;
        }

        return null;
    }

    private String getName(Document document) {
        String name = document.getElementsByClass("shop-content").attr("data-shop");
        if (name != null) {
            return name;
        }

        return null;
    }

    private Double getDiscount(Document document) {
        Matcher matcher = patternForDiscount.matcher(document.getElementsByClass("cashback-price").text());
        if (matcher.find()) {
            return Double.parseDouble(matcher.group());
        }
        return Double.NaN;
    }

    private String getLabel(Document document) {
        Matcher matcher = patternForLabel.matcher(document.getElementsByClass("cashback-price").text());
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}
