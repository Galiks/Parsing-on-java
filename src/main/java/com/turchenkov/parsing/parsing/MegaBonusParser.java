package com.turchenkov.parsing.parsing;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MegaBonusParser implements ParserInterface {
    @Override
    public ArrayList parsing() throws IOException, InterruptedException {
        Document document = Jsoup.connect("https://megabonus.com/")
                .userAgent("Chrome/32.0.1667.0 Safari/537.36").get();

        System.out.println(document);

        Elements elements = document.select("li");

        for (Element element : elements) {
            System.out.println(element.text());
        }

        System.out.println("The End");
        return null;
    }
}
