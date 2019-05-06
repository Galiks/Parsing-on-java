package com.turchenkov.parsing.domains.shop;

import lombok.*;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public final class Cash4Brands extends Shop {

    public Cash4Brands(String name, double discount, String label, String pageOnTheSite, String image) {
        super(name, discount, label, pageOnTheSite, image);
    }
}
