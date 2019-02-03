package com.turchenkov.parsing.parsingmethods;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.turchenkov.parsing.domains.shop.Shop;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.json.Json;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class KopikotParser implements ParserInterface {

    public int page = 0;

    /**
     * Name: title":"(.*?)"
     * Image: url":"(.*?)"
     * Discount: "amount":(.*?),
     * Label: "unit":"(.*?)"
     * Page: https:\\\/\\\/www\.kopikot\.ru\\\/stores\\\/(.*?)\)
     */

    @Override
    public List<Shop> parsing() {

        HttpResponse<String> response = null;
        for (int i = 0; i <= 0; i+=100) {

            String url = "https://d289b99uqa0t82.cloudfront.net/sites/5/campaigns_limit_100_offset_" + i + "_order_popularity.json";
            System.out.println(url);

            try {
                response = Unirest.get(url)
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("cache-control", "no-cache")
                        .header("Postman-Token", "2248ba09-4292-4e65-8fc5-d222e99631ec")
                        .asString();
            } catch (UnirestException e) {
                e.printStackTrace();
            }

            page++;

            System.out.println(response.getBody());
        }
        return null;
    }
}
