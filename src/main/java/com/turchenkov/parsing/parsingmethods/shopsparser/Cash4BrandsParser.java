package com.turchenkov.parsing.parsingmethods.shopsparser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class Cash4BrandsParser  {


    public ArrayList parsing() throws IOException, InterruptedException {
        Document document = Jsoup.connect("https://cash4brands.ru/cashback/")
                .data("key","Content-Type")
                .data("value","application/x-www-form-urlencoded")
                .data("description","")
                .userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36")
                .referrer("https://cash4brands.ru/cashback/")
                .method(Connection.Method.POST).execute().parse();

        Elements elements = document.select("section.shops").select("h3[itemprop]");

        for (Element element : elements) {
            System.out.println(element.text());
        }

//        System.out.println(document);
        return null;
    }
}
