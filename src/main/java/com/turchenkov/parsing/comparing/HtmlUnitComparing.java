package com.turchenkov.parsing.comparing;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.turchenkov.parsing.customannotation.ConsoleTimer;
import com.turchenkov.parsing.domains.shop.LetyShops;
import com.turchenkov.parsing.domains.shop.Shop;

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

//https://letyshops.com/shops?page=1

//Время: 119 секунд
public class HtmlUnitComparing {

    private final static Pattern patternForLabel = Pattern.compile("[$%€]|руб|(р.)|cent");
    private static ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @ConsoleTimer
    public static void main(String[] args) throws IOException {
        WebClient webClient = new WebClient();
//        webClient.getOptions().setThrowExceptionOnScriptError(false);
//        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        HtmlPage maxPage = webClient.getPage("https://letyshops.com/shops?page=1");
//        Long startTime = System.currentTimeMillis();
//        List<Future<List<Shop>>> futures = new ArrayList<>();
//        for (int i = 1; i <= getMaxPage(); i++) {
//            int finalI = i;
//            futures.add(pool.submit(() -> parseElements(finalI)));
//        }
//        List<Shop> pageResult = futures.stream().flatMap(getFutureStream()).collect(Collectors.toList());
//        pool.shutdown();
//        Long endTime = System.currentTimeMillis();
//        System.out.println("Время: " + (endTime - startTime) / 1000 + " секунд");
//        for (Shop shop : pageResult) {
//            System.out.println(shop);
//        }
    }

    private static Function<Future<List<Shop>>, Stream<? extends Shop>> getFutureStream() {
        return it -> {
            try {
                return it.get().stream();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return Stream.empty();
            }
        };
    }

    private static List<Shop> parseElements(int i) throws IOException {
        WebClient webClient = new WebClient();
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        System.out.println("Номер страницы: " + i);
        List<Shop> result = new ArrayList<>();
        String url = "https://letyshops.com/shops?page=" + i;
        HtmlPage page = webClient.getPage(url);
            DomNodeList<DomNode> domNodes = page.querySelectorAll("a.b-teaser__inner");
            for (DomNode domNode : domNodes) {
                String name = getName(domNode);
                String image = getImage(domNode);
                Double discount = getDiscount(domNode);
                String label = getLabel(domNode);
                String urlOfSite = getUrl(domNode);
                if (name != null & image != null & (discount != Double.NaN & discount != 0) & label != null) {
                    LetyShops letyShops = new LetyShops(name, discount, label, urlOfSite, image);
                    result.add(letyShops);
                }
            }
            System.out.println(result.size());
        return result;
    }

    private static String getUrl(DomNode domNode) {
        return domNode.getAttributes().item(2).getTextContent();
    }

    private static String getLabel(DomNode domNode) {
        String label;
        try {
            label = domNode.querySelector("div > span.b-shop-teaser__label.b-shop-teaser__label--red ").asText();
        } catch (NullPointerException e) {
            label = domNode.querySelector("div > div.b-shop-teaser__cash-value-row").asText();
        }
        Matcher matcher = patternForLabel.matcher(label);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    private static Double getDiscount(DomNode domNode) {
        try {
            return Double.parseDouble(domNode.querySelector("div > span.b-shop-teaser__new-cash").asText());
        } catch (NullPointerException e) {
            return Double.parseDouble(domNode.querySelector("div > span.b-shop-teaser__cash").asText());
        }
    }

    private static String getImage(DomNode domNode) {
        return domNode.querySelector("div.b-teaser__cover > img").getAttributes().item(0).getTextContent();
    }

    private static String getName(DomNode domNode) {
        return domNode.querySelector("div.b-teaser__title").asText();
    }

    private static int getMaxPage() throws IOException {
        WebClient webClient = new WebClient();
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        HtmlPage maxPage = webClient.getPage("https://letyshops.com/shops?page=1");
        DomNodeList<DomNode> pages = maxPage.querySelectorAll("a.b-pagination__link");
        return Integer.parseInt(pages.item(pages.size() - 2).getTextContent());
    }
}
