package com.turchenkov.parsing.repository;

import com.turchenkov.parsing.model.Shop;
import org.springframework.data.repository.CrudRepository;

public interface ShopRepository extends CrudRepository<Shop, Long> {
    Iterable<Shop> getAllByDiscount(double discount);
}
