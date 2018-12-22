package com.turchenkov.parsing.service;

import com.turchenkov.parsing.domains.shop.Shop;

import java.util.List;

public interface ParsingService {
    void parsingAndSaveInDB();
    void deleteAllFromDB();
    List<Shop> getListOfShop();
    List<Shop> update();
}
