package com.turchenkov.parsing.domains.shop;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;

@Entity
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class MegaBonus extends Shop {
    public MegaBonus(String name, double discount, String label, String pageOnTheSite, String image) {
        super(name, discount, label, pageOnTheSite, image);
    }

}
