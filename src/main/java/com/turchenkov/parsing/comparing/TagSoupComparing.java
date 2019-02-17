package com.turchenkov.parsing.comparing;

import java.net.URL;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import org.ccil.cowan.tagsoup.jaxp.SAXParserImpl;

public class TagSoupComparing {
    public static void main(String args[]) throws Exception {

        // print the 'src' attributes of <img> tags
        // from http://www.yahoo.com/
        // using the TagSoup parser

        SAXParserImpl.newInstance(null).parse(
                new URL("http://www.yahoo.com/").openConnection().getInputStream(),
                new DefaultHandler() {
                    public void startElement(String uri, String localName,
                                             String name, Attributes a)
                    {
                        if (name.equalsIgnoreCase("img"))
                            System.out.println(a.getValue("src"));
                    }
                }
        );
    }
}
