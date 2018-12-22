package com.turchenkov.parsing.domains.shop;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Shops")
public abstract class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;


    private double discount;


    private String label;


    private String pageOnTheSite;


    private String image;

    public Shop(String name, double discount, String label, String pageOnTheSite, String image) {
        this.name = name;
        this.discount = discount;
        this.label = label;
        this.pageOnTheSite = pageOnTheSite;
        this.image = image;
    }

    public Shop() {
    }
}
