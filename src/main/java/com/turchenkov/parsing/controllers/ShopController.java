package com.turchenkov.parsing.controllers;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.turchenkov.parsing.service.ShopServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
public class ShopController {

    @Autowired
    private ShopServiceImpl service;

    @GetMapping("/shops")
    public String allReportsGet(Model model) {
        model.addAttribute("shops", service.getListOfShop());
        return "shops";
    }

    @PostMapping("/shops")
    public String allReportsPost() {
        return "redirect:/shops";
    }

    @PostMapping("/update")
    public String updateShopsPost() {
        service.update();
        return "redirect:/shops";
    }

    @GetMapping("/shops/orderByDiscount")
    public String orderByDiscountGet(Model model) {
        model.addAttribute("shops", service.orderByDiscount());
        return "shops";
    }

    @GetMapping("/shops/orderByDiscountDesc")
    public String orderByDiscountDescGet(Model model) {
        model.addAttribute("shops", service.orderByDiscountDesc());
        return "shops";
    }

    @PostMapping("/excel")
    public String saveShopsInExcelFile(){
        service.saveInExcelFile();
        return "redirect:/shops";
    }

    @PostMapping("/csv")
    public String saveShopsInCSVFile() throws IOException {
        service.saveInCSVFile();
        return "redirect:/shops";
    }
}
