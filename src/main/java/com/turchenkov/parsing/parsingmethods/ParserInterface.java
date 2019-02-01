package com.turchenkov.parsing.parsingmethods;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.turchenkov.parsing.customannotation.Timer;
import com.turchenkov.parsing.domains.shop.Shop;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public interface ParserInterface {

    List<Shop> parsing();
}
