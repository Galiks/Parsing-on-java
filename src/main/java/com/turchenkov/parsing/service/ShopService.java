package com.turchenkov.parsing.service;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.turchenkov.parsing.domains.shop.Shop;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ShopService {
    void parsingAndSaveInDB() throws UnirestException;
    void deleteAllFromDB();
    List<Shop> getListOfShop();
    List<Shop> update() throws UnirestException;
    List<Shop> orderByDiscount();
    List<Shop> orderByDiscountDesc();
}
