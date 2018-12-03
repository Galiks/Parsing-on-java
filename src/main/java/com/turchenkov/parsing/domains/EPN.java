package com.turchenkov.parsing.domains;

public class EPN extends SiteForParsing {

    public EPN() {
    }

    public EPN(String name, double discount, String label, String pageOnTheSite, String image) {
        super(name, discount, label, pageOnTheSite, image);
    }

    @Override
    public String toString() {
        return "EPN{" +
                "name='" + getName() + '\'' +
                ", discount=" + getDiscount() +
                ", label='" + getLabel() + '\'' +
                ", pageOnTheSite='" + getPageOnTheSite() + '\'' +
                ", image='" + getImage() + '\'' +
                '}';
    }
}
