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
                "name='" + getName() + '\'' +
                ", discount=" + getDiscount() +
                ", label='" + getLabel() + '\'' +
                ", pageOnTheSite='" + getPageOnTheSite() + '\'' +
                ", image='" + getImage() + '\'' +
                '}';
    }
}
