package com.turchenkov.parsing.controllers;

import com.turchenkov.parsing.model.Shop;
import com.turchenkov.parsing.parsing.ParsingAllSite;
import com.turchenkov.parsing.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ShopController {

    @Autowired
    private ShopRepository shopRepository;

    private ParsingAllSite parsingAllSite = new ParsingAllSite();

//    @GetMapping("/shops")
//    public String parsingGet(Model model){
//        model.addAttribute("shops", new Shop());
//        return "shops";
//    }
//
//    @PostMapping("/shops")
//    public String parsingPost(@ModelAttribute Shop shop){
//
//    }

    @GetMapping("/shops")
    public String allShopsGet(Model model){
        List<Shop> shops = new ArrayList<>();
        shopRepository.findAll().forEach(shops::add);
        model.addAttribute("shops", shops);
        return "shops";
    }

    @PostMapping("/reports")
    public String allShopsPost(){
        return "redirect:/shops";
    }
}
