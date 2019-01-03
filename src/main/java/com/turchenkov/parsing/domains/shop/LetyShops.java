package com.turchenkov.parsing.domains.shop;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class LetyShops extends Shop {

    public LetyShops() {
    }

    public LetyShops(String name, double discount, String label, String pageOnTheSite, String image) {
        super(name, discount, label, pageOnTheSite, image);
    }

    public LetyShops(Long id, String name, double discount, String label, String pageOnTheSite, String image) {
        super(id, name, discount, label, pageOnTheSite, image);
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
