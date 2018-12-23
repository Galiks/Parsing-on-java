package com.turchenkov.parsing.controllers;

import com.turchenkov.parsing.service.ParsingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ShopController {

    @Autowired
    private ParsingServiceImpl service;

    @GetMapping({"/shops", "/"})
    public String allReportsGet(Model model) {
        model.addAttribute("shops", service.getListOfShop());
        return "shops";
    }

    @PostMapping("/shops")
    public String allReportsPost() {
        return "redirect:/shops";
    }

    @PostMapping("/shops/update")
    public String updateShops() {
        service.update();
        return "redirect:/shops";
    }

    @PostMapping("/shops/orderByDiscount")
    public String orderByDiscount() {
        return "redirect:/shops";
    }

}
