package com.turchenkov.parsing.domains.shop;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public final class EPN extends Shop {

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
