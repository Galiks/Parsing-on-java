package com.turchenkov.parsing.parsing;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Cash4BrandsParser implements ParserInterface {
    @Override
    public ArrayList parsing() throws IOException, InterruptedException {
        Document document = Jsoup.connect("https://cash4brands.ru/cashback/")
                .data("namerequest", "show_page")
                .data("csrfmiddlewaretoken", "sjA7lDtjDF0qmc3GMNJLaQp1xt8gYux3UCD5KEpsrgInQ2nAULtZtJJWSUrmqag1")
                .userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36")
                .referrer("https://cash4brands.ru/")
                .method(Connection.Method.POST).execute().parse();

        Elements elements = document.select("section.shops").select("h3[itemprop]");

        for (Element element : elements) {
            System.out.println(element.text());
        }

        //System.out.println(document);
        return null;
    }
}
