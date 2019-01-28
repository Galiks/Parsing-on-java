package com.turchenkov.parsing.parsingmethods;

import com.turchenkov.parsing.domains.shop.Shop;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MegaBonusParser implements ParserInterface {

    private final String patternForShopsPage = ":\"\\\\\\/shop\\\\\\/[^'!@\"#$%^&*()=+№;:?\\\\\\/]+";
    private final String userAgent = "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36";

    @Override
    public List<Shop> parsing() throws IOException {
//        Pattern pattern = Pattern.compile(patternForShopsPage);
//
//        List<Shop> result = new ArrayList<>();
//
//        System.setProperty("webdriver.chrome.driver", "E:/Download/chromedriver_win32/chromedriver.exe");
//        WebDriver driver = new ChromeDriver();
//        driver.navigate().to("https://megabonus.com/feed");
//
//        Set<String> pages = new HashSet<>();
//        Matcher matcher1 = pattern.matcher(driver.getPageSource());
//        while (matcher1.find()) {
//            pages.add("https://megabonus.com/shop" + matcher1.group().substring(9));
//        }
//
//        System.out.println("Result size: " + pages.size());
//
//        List<String> newPages = new ArrayList<>(pages);
//
//        for (String page : newPages) {
//            parseElements(page);
//        }

//        "https://megabonus.com/shop/xistoreby?utm_referrer=&fa821dba_ipp_key=v1548584423101%2fv3394bdaf330c859c7e1f061638eca2afa04ab3%2fukK3Po4P9qM%2bnFsQSL0xNA%3d%3d&fa821dba_ipp_uid=1548584423101%2f2mnS1fKfLUqmovXK%2f7XfKbe8edmcz%2bnCkLXEoGg%3d%3d&fa821dba_ipp_uid1=1548584423101&fa821dba_ipp_uid2=2mnS1fKfLUqmovXK%2f7XfKbe8edmcz%2bnCkLXEoGg%3d%3d"

        Pattern patternForTrueShopPage = Pattern.compile("\"(.*?)\"");
        Pattern patternForFunctionOfUrl = Pattern.compile("url \\+=(.*?);");
        Pattern patternForInitialParametrOfUrl = Pattern.compile("\\(\"(.*?)\"\\)");

        Document document = Jsoup.connect("https://megabonus.com/shop/mvideo").get();
//        System.out.println(document.location());
        Document document1 = Jsoup.connect(document.location()).userAgent(userAgent).get();
        System.out.println(document1.outerHtml());
        System.out.println();
        String resultURL = "";

        Matcher matcher2 = patternForInitialParametrOfUrl.matcher(document1.outerHtml());
        if (matcher2.find()){
            resultURL += matcher2.group(1);
        }

        Matcher matcher = patternForFunctionOfUrl.matcher(document1.outerHtml());

        if (matcher.find()){
//            System.out.println(matcher.group(1));
            Matcher matcher1 = patternForTrueShopPage.matcher(matcher.group(1));
            while (matcher1.find()){
                resultURL += matcher1.group(1);
            }
        }


        System.out.println(resultURL);



//        Document document2 = Jsoup.connect("https://megabonus.com/shop/xistoreby?utm_referrer=&fa821dba_ipp_key=v1548584423101%2fv3394bdaf330c859c7e1f061638eca2afa04ab3%2fukK3Po4P9qM%2bnFsQSL0xNA%3d%3d&fa821dba_ipp_uid=1548584423101%2f2mnS1fKfLUqmovXK%2f7XfKbe8edmcz%2bnCkLXEoGg%3d%3d&fa821dba_ipp_uid1=1548584423101&fa821dba_ipp_uid2=2mnS1fKfLUqmovXK%2f7XfKbe8edmcz%2bnCkLXEoGg%3d%3d").get();
//        System.out.println(document2.outerHtml());


//        System.out.println(document1.outerHtml());
//        for (Element element : name) {
//            System.out.println(element.text());
//        }


            return null;
    }

    private Shop parseElements(String shop) throws IOException {

        Document document = Jsoup.connect(shop).userAgent(userAgent).post();
        System.out.println(document.outerHtml());
        System.out.println();
        return null;
    }
}
