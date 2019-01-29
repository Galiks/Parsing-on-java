package com.turchenkov.parsing.parsingmethods;

import com.turchenkov.parsing.domains.shop.MegaBonus;
import com.turchenkov.parsing.domains.shop.Shop;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MegaBonusParser implements ParserInterface {

    private final String patternForShopsPage = ":\"\\\\\\/shop\\\\\\/[^'!@\"#$%^&*()=+№;:?\\\\\\/]+";
    private final Pattern pattern = Pattern.compile(patternForShopsPage);
    private final String userAgent = "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36";
    private final Pattern patternForTrueShopPage = Pattern.compile("\"(.*?)\"");
    private final Pattern patternForFunctionOfUrl = Pattern.compile("url \\+=(.*?);");
    private final Pattern patternForInitialParameterOfUrl = Pattern.compile("\\(\"(.*?)\"\\)");
    private final Pattern patternForDiscount = Pattern.compile("\\d+[.]*\\d*");
    private final Pattern patternForLabel = Pattern.compile("[$%€]|руб|(р.)|cent|р|Р");

    @Override
    public List<Shop> parsing() throws IOException {


        List<Shop> result = new ArrayList<>();

        System.setProperty("webdriver.chrome.driver", "E:/Download/chromedriver_win32/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.navigate().to("https://megabonus.com/feed");

        Set<String> pages = new HashSet<>();
        Matcher matcher1 = pattern.matcher(driver.getPageSource());
        while (matcher1.find()) {
            pages.add("https://megabonus.com/shop" + matcher1.group().substring(9));
        }

        List<String> newPages = new ArrayList<>(pages);


        List<String> pages1 = getTruePages(newPages);

        for (String s : pages1) {
            parseElements(s);
        }


        return null;
    }

    private List<String> getTruePages(List<String> dirtyPages) throws IOException {
        List<String> result = new ArrayList<>();

        for (String dirtyPage : dirtyPages) {
            StringBuilder resultURL = new StringBuilder();


            Document document = Jsoup.connect(dirtyPage).get();
            String document1 = Jsoup.connect(document.location()).userAgent(userAgent).get().outerHtml();


            Matcher matcher2 = patternForInitialParameterOfUrl.matcher(document1);
            if (matcher2.find()) {
                resultURL.append(matcher2.group(1));
            }

            Matcher matcher = patternForFunctionOfUrl.matcher(document1);

            if (matcher.find()) {
                Matcher matcher1 = patternForTrueShopPage.matcher(matcher.group(1));
                while (matcher1.find()) {
                    resultURL.append(matcher1.group(1));
                }
            }

            result.add(resultURL.toString());
        }

        return result;
    }

    private Shop parseElements(String shop) {

        Document document;
        try {
            document = Jsoup.connect(shop).get();
        } catch (IOException e) {
            return null;
        }
        String name = document.getElementsByClass("shop-content").attr("data-shop");
        String image = document.getElementsByClass("shop-img").first().getElementsByTag("img").attr("src");
        Double discount = getDiscount(document);
        String label = getLabel(document);
        System.out.println(name);
        if (name != null & image != null & discount != Double.NaN & label != null) {
            return new MegaBonus(name, discount, label, shop, image);
        }
        return null;
    }

    private Double getDiscount(Document document){
        Matcher matcher = patternForDiscount.matcher(document.getElementsByClass("cashback-price").text());
        if (matcher.find()){
            return Double.parseDouble(matcher.group());
        }
        return Double.NaN;
    }

    private String getLabel(Document document){
        Matcher matcher = patternForLabel.matcher(document.getElementsByClass("cashback-price").text());
        if (matcher.find()){
            return matcher.group();
        }
        return null;
    }
}
