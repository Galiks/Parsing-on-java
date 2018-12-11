package com.turchenkov.parsing.parsing;

import com.turchenkov.parsing.domains.SiteForParsing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParsingAllSite {

    public List<SiteForParsing> parseAllSites() throws IOException, InterruptedException {
        LetyShopsParser letyShopsParser = new LetyShopsParser();
        EPN_Parser epn_parser = new EPN_Parser();

        List<ParserInterface> parsers = new ArrayList<>();
        parsers.add(letyShopsParser);
        parsers.add(epn_parser);

        ArrayList<SiteForParsing> shops = new ArrayList<>();

        for (ParserInterface parser : parsers) {
            for (Object shop : parser.parsing()) {
                shops.add((SiteForParsing) shop);
            }
        }

        return shops;
    }
}
