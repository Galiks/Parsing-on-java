package com.turchenkov.parsing.parsing;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Parser {
    public static void main(String[] args) throws IOException {
        Document document = Jsoup.connect("https://letyshops.com/shops?page=1").get();

        Elements elements = document.getElementsByClass("b-teaser__title");

        System.out.println("Names: ");
        for (Element element : elements) {
            System.out.println(element.text());
        }

        elements = document.getElementsByClass("b-shop-teaser__cash-value-row");

        System.out.println("Discounts: ");
        for (Element element : elements) {
            System.out.println(element.text());
        }
    }
}
