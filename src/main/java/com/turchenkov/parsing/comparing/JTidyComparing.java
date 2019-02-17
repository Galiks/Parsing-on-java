package com.turchenkov.parsing.comparing;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Node;
import org.w3c.tidy.Tidy;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class JTidyComparing {

    public static void main(String[] args) {
        Tidy tidy = new Tidy();
        tidy.setInputEncoding("utf-8");			// -utf8
        tidy.setOutputEncoding("utf-8");		// -utf8
        HttpClientComparing httpClientComparing = new HttpClientComparing();
        String html = httpClientComparing.main();
        tidy.setXHTML(true);
        Document parse = tidy.parseDOM(new ByteArrayInputStream(html.getBytes()), System.out);
        System.out.println(parse);

    }
}
