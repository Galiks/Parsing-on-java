package com.turchenkov.parsing;

import com.turchenkov.parsing.domains.SiteForParsing;
import com.turchenkov.parsing.model.Shop;
import com.turchenkov.parsing.parsing.ParsingAllSite;
import com.turchenkov.parsing.repository.ShopRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ParsingApplication {

    @RequestMapping("/shops")
    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(ParsingApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(ShopRepository shopRepository) {
        return args -> {
            ParsingAllSite parsingAllSite = new ParsingAllSite();
            List<SiteForParsing> siteForParsings = parsingAllSite.parseAllSites();
            for (SiteForParsing siteForParsing : siteForParsings) {
                Shop shop = new Shop(siteForParsing.getName(), siteForParsing.getDiscount(), siteForParsing.getLabel(), siteForParsing.getPageOnTheSite(), siteForParsing.getImage());
                shopRepository.save(shop);
                //shops.add(shop);
            }
        };
    }
}
