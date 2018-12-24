package com.turchenkov.parsing.repository;

import com.turchenkov.parsing.domains.shop.Shop;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends CrudRepository<Shop, Long> {
    Iterable<Shop> getAllByDiscount(double discount);

    Iterable<Shop> findAllByOrderByName();

    @Override
    void deleteAll();
}
