package com.turchenkov.parsing.parsingmethods;

import com.jayway.jsonpath.JsonPath;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.turchenkov.parsing.customannotation.Timer;
import com.turchenkov.parsing.domains.shop.Kopikot;
import com.turchenkov.parsing.domains.shop.Shop;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.json.Json;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class KopikotParser implements ParserInterface {

    private final ExecutorService pool;

    public KopikotParser() {
        int THREADS = 4;
        pool = Executors.newFixedThreadPool(THREADS);
    }

    @Timer
    @Override
    public List<Shop> parsing() {

        List<Shop> result = new ArrayList<>();
        List<Future<List<Shop>>> futures = new ArrayList<>();

        HttpResponse<String> response = null;
        for (int i = 0; i <= 1300; i+=100) {

            String url = "https://d289b99uqa0t82.cloudfront.net/sites/5/campaigns_limit_100_offset_" + i + "_order_popularity.json";

            try {
                response = Unirest.get(url)
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("cache-control", "no-cache")
                        .header("Postman-Token", "2248ba09-4292-4e65-8fc5-d222e99631ec")
                        .asString();
            } catch (UnirestException e) {
                e.printStackTrace();
            }

            HttpResponse<String> finalResponse = response;
            futures.add(pool.submit( () -> parseElements(finalResponse.getBody())));

        }

        for (Future<List<Shop>> future : futures) {
            try {
                result.addAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @PreDestroy
    private void destroy(){
        pool.shutdown();
    }

    private List<Shop> parseElements(String response) {
        List<Shop> elements = new ArrayList<>();
        List<Object> items = JsonPath.read(response, "$..items[*]");
        for (Object item : items) {
            String name = getName(item);
            String shopPage = getShopPage(item);
            String image = getImage(item);
            Double discount = getDiscount(item);
            String label = getLabel(item);
            if (name != null & image != null & (discount != Double.NaN & discount != 0) & label != null & shopPage != null) {
                elements.add(new Kopikot(name, discount, label, shopPage, image));
            }
        }
        return elements;
    }

    private String getLabel(Object item) {
        String label = JsonPath.read(item, "$.commission.max.unit");
        if (label != null) {
            return label;
        }
        return null;
    }

    private Double getDiscount(Object item) {
        Object discount = JsonPath.read(item, "$.commission.max.amount");
        if (discount != null) {
            try{
                return Double.parseDouble(discount.toString());
            } catch (NumberFormatException e){
                e.printStackTrace();
                return Double.NaN;
            }
        }
        return Double.NaN;
    }

    private String getImage(Object item) {
        String image = JsonPath.read(item, "$.image.url");
        if (image != null) {
            return image;
        }
        return null;
    }

    private String getShopPage(Object item) {
        String page = JsonPath.read(item, "$.url");
        String id = JsonPath.read(item, "$.id");
        if (page != null & id != null) {
            return "https://www.kopikot.ru/stores/"+page+"/"+id;
        }

        return null;
    }

    private String getName(Object item) {
        String name = JsonPath.read(item, "$.title");
        if (name != null) {
            return name;
        }
        return null;
    }
}
