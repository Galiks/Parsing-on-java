package com.turchenkov.parsing.parsing;

import com.turchenkov.parsing.model.LetyShops;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    public static void main(String[] args) throws IOException {

        

    }

    private static void parsingLetyShops() throws IOException {
        Pattern patternForDiscount = Pattern.compile("\\d+[.]*\\d*");
        Pattern patternForLabel = Pattern.compile("[$%€]|руб");

        List<String> discounts = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        List<LetyShops> letyShops = new ArrayList<>();

        Document document = Jsoup.connect("https://letyshops.com/shops?page=1").get();

        Elements elements = document.getElementsByClass("b-pagination__item");

        int maxPage = Integer.parseInt(elements.get(elements.size()-2).text());

        for (int i = 1; i <= maxPage; i++){

            document = Jsoup.connect("https://letyshops.com/shops?page=" + i).get();

            elements = document.getElementsByClass("b-teaser__title");

            for (Element element : elements) {
                names.add(element.text());
            }

            elements = document.getElementsByClass("b-shop-teaser__cash-value-row");

            for (Element element : elements) {
                discounts.add(element.text());
            }

            elements = document.getElementsByClass("b-shop-teaser__label");

            for (Element element : elements) {
                Matcher matcher = patternForLabel.matcher(element.text());
                if (matcher.find()) {
                    labels.add(element.text().substring(matcher.start(), matcher.end()));
                }
            }
        }

        System.out.println(labels.size() + " : " + discounts.size() + " : " + names.size());


        for (int i = 0; i < discounts.size(); i++){
            Matcher matcher = patternForDiscount.matcher(discounts.get(i));
            if (matcher.find()){
                letyShops.add(new LetyShops(names.get(i), Double.parseDouble(discounts.get(i).substring(matcher.start(),matcher.end())), labels.get(i)));
            }
        }

        for (LetyShops letyShop : letyShops) {
            System.out.println(letyShop);
        }
    }
}
