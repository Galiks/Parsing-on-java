package com.turchenkov.parsing.domains;

public class LetyShops extends SiteForParsing {

    public LetyShops() {
    }

    public LetyShops(String name, double discount, String label, String pageOnTheSite, String image) {
        super(name, discount, label, pageOnTheSite, image);
    }

    @Override
    public String toString() {
        return "LetyShops{" +
                "name='" + name + '\'' +
                ", discount=" + discount +
                ", label='" + label + '\'' +
                ", pageOnTheSite='" + pageOnTheSite + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
