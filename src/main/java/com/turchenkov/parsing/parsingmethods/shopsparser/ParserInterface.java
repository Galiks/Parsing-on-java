package com.turchenkov.parsing.parsingmethods.shopsparser;

import com.turchenkov.parsing.domains.shop.Shop;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public interface ParserInterface {
    List<Shop> parsing() throws IOException, InterruptedException;
}
