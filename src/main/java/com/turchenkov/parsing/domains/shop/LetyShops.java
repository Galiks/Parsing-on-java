package com.turchenkov.parsing.domains.shop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class LetyShops extends Shop {

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
