package com.turchenkov.parsing.domains;

public abstract class SiteForParsing {

    String name;
    double discount;
    String label;
    String pageOnTheSite;
    String image;

    public SiteForParsing(String name, double discount, String label, String pageOnTheSite, String image) {
        this.name = name;
        this.discount = discount;
        this.label = label;
        this.pageOnTheSite = pageOnTheSite;
        this.image = image;
    }

    public SiteForParsing() {
    }
}
