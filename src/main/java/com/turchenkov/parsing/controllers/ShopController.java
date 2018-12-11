package com.turchenkov.parsing.controllers;

import com.turchenkov.parsing.domains.SiteForParsing;
import com.turchenkov.parsing.dto.ShopDTO;
import com.turchenkov.parsing.model.Shop;
import com.turchenkov.parsing.parsingmethods.ParsingAllSite;
import com.turchenkov.parsing.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ShopController {

    @Autowired
    private ShopRepository shopRepository;

    private ParsingAllSite parsingAllSite = new ParsingAllSite();

    @GetMapping("/shops")
    public String allReportsGet(Model model) throws IOException, InterruptedException {
        getParsing();
        List<Shop> shops = new ArrayList<>();
        shopRepository.findAll().forEach(shops::add);
        model.addAttribute("shops", shops);
        model.addAttribute("shopDto", new ShopDTO());
        return "shops";
    }

    private void getParsing() throws IOException, InterruptedException {
        for (SiteForParsing site : parsingAllSite.parseAllSites()) {
            shopRepository.save(new Shop(site.getName(), site.getDiscount(), site.getLabel(), site.getPageOnTheSite(), site.getImage()));
        }
    }

    @PostMapping("/shops")
    public String allReportsPost(@ModelAttribute ShopDTO shopDTO) {
        return "redirect:/shops";
    }


}
