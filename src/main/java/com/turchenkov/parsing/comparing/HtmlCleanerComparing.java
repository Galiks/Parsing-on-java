package com.turchenkov.parsing.comparing;

import org.htmlcleaner.*;

import javax.xml.soap.Node;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;

public class HtmlCleanerComparing {
    public static void main(String[] args) throws IOException, XPatherException {
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        URL url = new URL("https://letyshops.com/shops?page=1");
        TagNode clean = htmlCleaner.clean(url, "UTF-8");
        Object[] xPath = clean.evaluateXPath("//a[@class='b-teaser__inner']");
        System.out.println(xPath.length);
        for (int i = 0; i < xPath.length; i++){
            TagNode tagNode = (TagNode) xPath[i];
            System.out.println(Arrays.toString(tagNode.evaluateXPath("//div[@class='b-teaser__title']")));
        }
    }
}
