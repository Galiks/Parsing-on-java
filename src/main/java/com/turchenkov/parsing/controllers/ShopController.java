package com.turchenkov.parsing.controllers;

import com.turchenkov.parsing.service.ShopServiceImpl;
import com.turchenkov.parsing.service.TimerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
public class ShopController {

    @Autowired
    private ShopServiceImpl shopService;

    @Autowired
    private TimerServiceImpl timerService;

    @GetMapping("/shops")
    public String allReportsGet(Model model) {
        model.addAttribute("shops", shopService.getListOfShop());
        return "shops";
    }

    @PostMapping("/shops")
    public String allReportsPost() {
        return "redirect:/shops";
    }

    @PostMapping("/update")
    public String updateShopsPost() {
        timerService.deleteAllFromDB();
        shopService.update();
        return "redirect:/shops";
    }

    @GetMapping("/shops/orderByDiscount")
    public String orderByDiscountGet(Model model) {
        model.addAttribute("shops", shopService.orderByDiscount());
        return "shops";
    }

    @GetMapping("/shops/orderByDiscountDesc")
    public String orderByDiscountDescGet(Model model) {
        model.addAttribute("shops", shopService.orderByDiscountDesc());
        return "shops";
    }

    @PostMapping("/excel")
    public String saveShopsInExcelFile(){
        shopService.saveInExcelFile();
        return "redirect:/shops";
    }

    @PostMapping("/csv")
    public String saveShopsInCSVFile() throws IOException {
        shopService.saveInCSVFile();
        return "redirect:/shops";
    }

    @PostMapping("/timer")
    public String saveTimersInExcelFile(){
        timerService.saveInExcelFile();
        return "redirect:/shops";
    }
}
