package com.turchenkov.parsing.domains.shop;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public final class EPN extends Shop {

    public EPN() {
    }

    public EPN(String name, double discount, String label, String pageOnTheSite, String image) {
        super(name, discount, label, pageOnTheSite, image);
    }
}
