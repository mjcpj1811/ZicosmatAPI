package com.example.ZicosmatAPI.repository;

import com.example.ZicosmatAPI.model.ProductVariants;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariantRepository extends JpaRepository<ProductVariants,Long> {

    ProductVariants findBySku(String sku);
}
