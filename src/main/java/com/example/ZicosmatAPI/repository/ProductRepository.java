package com.example.ZicosmatAPI.repository;

import com.example.ZicosmatAPI.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findProductByName(String name);

    Product findProductByNameAndThickness(String name,String thickness);
}
