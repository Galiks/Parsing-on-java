package com.turchenkov.parsing.comparing;

import com.jayway.jsonpath.JsonPath;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HttpClientComparing {

    //https://letyshops.com/shops?page=1

    private String url = "https://letyshops.com/shops?page=1";

    public String main() {

        // Create an instance of HttpClient.
        HttpClient client = new HttpClient();

        // Create a method instance.
        GetMethod method = new GetMethod(url);

        // Provide custom retry handler is necessary
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));


        try {
            // Execute the method.
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + method.getStatusLine());
            }

            // Read the response body.
            byte[] responseBody = method.getResponseBody();

            // Deal with the response.
            // Use caution: ensure correct character encoding and is not binary data
            return new String(responseBody);
            //<editor-fold desc="Try parse html using Jsoup">
            //            Elements elements = Jsoup.parse(new String(responseBody)).getElementsByClass("b-teaser__inner");
//            for (Element element : elements) {
//                System.out.println(element.getElementsByClass("b-teaser__title").text());
//            }
            //</editor-fold>
        } catch (HttpException e) {
            System.err.println("Fatal protocol violation: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Fatal transport error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Release the connection.
            method.releaseConnection();
        }

        return null;
    }
}
