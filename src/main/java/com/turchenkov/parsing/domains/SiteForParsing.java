package com.turchenkov.parsing.domains;

public abstract class SiteForParsing {

    String name;
    double discount;
    String label;

    public SiteForParsing(String name, double discount, String label) {
        this.name = name;
        this.discount = discount;
        this.label = label;
    }

    public SiteForParsing() {
    }
}
