package com.turchenkov.parsing.service;

import com.turchenkov.parsing.domains.shop.Shop;
import com.turchenkov.parsing.parsingmethods.shopsparser.ParserInterface;
import com.turchenkov.parsing.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ParsingServiceImpl implements ParsingService {

    @Autowired
    ShopRepository shopRepository;

    @Autowired
    private List<ParserInterface> parsers;

    @Override
    public void parsingAndSaveInDB() {
        for (ParserInterface parser : parsers) {
            try {
                shopRepository.saveAll(parser.parsing());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteAllFromDB() {
        shopRepository.deleteAll();
    }

    @Override
    public List<Shop> getListOfShop() {
        return (List<Shop>) shopRepository.findAllByOrderByName();
    }

    @Override
    public List<Shop> update() {
        deleteAllFromDB();
        parsingAndSaveInDB();
        return getListOfShop();
    }


}
