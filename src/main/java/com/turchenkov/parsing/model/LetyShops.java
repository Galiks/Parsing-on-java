package com.turchenkov.parsing.model;

public class LetyShops {

    String name;

    double discount;

    String label;

    public LetyShops() {
    }

    public LetyShops(String name, double discount, String label) {
        this.name = name;
        this.discount = discount;
        this.label = label;
    }

    @Override
    public String toString() {
        return "LetyShops{" +
                "name='" + name + '\'' +
                ", discount=" + discount + " " + label +
                '}';
    }
}
