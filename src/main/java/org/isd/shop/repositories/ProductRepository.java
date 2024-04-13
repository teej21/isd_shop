package org.isd.shop.repositories;

import org.isd.shop.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>{
    List<Product> findByCategoryId(Long id);
}
