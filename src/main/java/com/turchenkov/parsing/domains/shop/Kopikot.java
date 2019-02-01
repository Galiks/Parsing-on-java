package com.turchenkov.parsing.domains.shop;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public final class Kopikot extends Shop {

    public Kopikot(String name, double discount, String label, String pageOnTheSite, String image) {
        super(name, discount, label, pageOnTheSite, image);
    }

    public Kopikot(Long id, String name, double discount, String label, String pageOnTheSite, String image) {
        super(id, name, discount, label, pageOnTheSite, image);
    }
}
