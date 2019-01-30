package com.turchenkov.parsing.parsingmethods;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.turchenkov.parsing.domains.shop.Shop;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class CashbackoffParser implements ParserInterface {
    @Override
    public List<Shop> parsing() {
        Document document = null;
        try {
            document = Jsoup.connect("https://cashbackoff.ru/index_shops_search.php").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (document != null) {
            Elements text = document.getElementsByClass("stores-list-item-title");

            for (Element element : text) {
                System.out.println(element.text());
            }
        }



        return null;
    }
}
