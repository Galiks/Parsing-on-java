package com.turchenkov.parsing.parsingmethods;

import com.turchenkov.parsing.domains.SiteForParsing;

import java.io.IOException;
import java.util.List;

public interface ParserInterface {
    List<SiteForParsing> parsing() throws IOException, InterruptedException;
}
