package com.turchenkov.parsing.parsing;

import com.turchenkov.parsing.domains.SiteForParsing;
import com.turchenkov.parsing.model.Shop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParsingAllSite {
    private LetyShopsParser letyShopsParser = new LetyShopsParser();
    private EPN_Parser epn_parser = new EPN_Parser();

    private List<ParserInterface> parsers = new ArrayList<ParserInterface>();



    public ArrayList parseAllSites() throws IOException, InterruptedException {
        parsers.add(letyShopsParser);
        //parsers.add(epn_parser);

        ArrayList<SiteForParsing> shops = new ArrayList<>();

        for (ParserInterface parser : parsers) {
            for (Object shop : parser.parsing()) {
                shops.add((SiteForParsing) shop);
            }
        }

        return shops;
    }
}
