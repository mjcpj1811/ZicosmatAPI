package com.example.ZicosmatAPI.repository;

import com.example.ZicosmatAPI.model.Image;
import com.example.ZicosmatAPI.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByProduct(Product product);

    Image findByImageUrl(String imageUrl);
}
