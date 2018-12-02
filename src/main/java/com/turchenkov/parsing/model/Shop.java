package com.turchenkov.parsing.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Shops")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Discount")
    private double discount;

    @Column(name = "Label")
    private String label;

    @Column(name = "Url_on_site")
    private String pageOnTheSite;

    @Column(name = "Image")
    private String image;

    public Shop() {
    }

    public Shop(String name, double discount, String label, String pageOnTheSite, String image) {
        this.name = name;
        this.discount = discount;
        this.label = label;
        this.pageOnTheSite = pageOnTheSite;
        this.image = image;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "name='" + name + '\'' +
                ", discount=" + discount +
                ", label='" + label + '\'' +
                ", pageOnTheSite='" + pageOnTheSite + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
