package com.turchenkov.parsing.domains.shop;

import lombok.*;

import javax.persistence.Entity;

@Entity
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public final class LetyShops extends Shop {

    public LetyShops(String name, double discount, String label, String pageOnTheSite, String image) {
        super(name, discount, label, pageOnTheSite, image);
    }
}
