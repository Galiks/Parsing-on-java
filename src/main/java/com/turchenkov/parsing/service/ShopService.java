package com.turchenkov.parsing.service;

import com.turchenkov.parsing.domains.shop.Shop;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ShopService {
    void parsingAndSaveInDB();
    void deleteAllFromDB();
    List<Shop> getListOfShop();
    List<Shop> update();
    List<Shop> orderByDiscount();
    List<Shop> orderByDiscountDesc();
}
