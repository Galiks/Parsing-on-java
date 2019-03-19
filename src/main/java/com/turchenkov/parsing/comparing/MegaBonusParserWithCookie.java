package com.turchenkov.parsing.comparing;

import com.turchenkov.parsing.domains.shop.Shop;
import com.turchenkov.parsing.parsingmethods.ParserInterface;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MegaBonusParserWithCookie implements ParserInterface {

    private final String addressOfSite = "https://megabonus.com/feed";
    private final String anotherAddress = "https://megabonus.com/index/get_more_offers";

    @Override
    public List<Shop> parsing() {
        Connection.Response res = null;
        try {
            res = Jsoup.connect(anotherAddress)
                    .data("p","1")
                    .data("sortby", "popularity")
                    .data("per_page", "23")
                    .method(Connection.Method.POST)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document doc = null;

        try {
            doc = res.parse();
            System.out.println(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> sessionId = res.cookies();
        System.out.println( sessionId);

        return null;
    }
}
