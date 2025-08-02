package com.example.ZicosmatAPI.dto.response;

import com.example.ZicosmatAPI.model.Image;
import com.example.ZicosmatAPI.model.ProductVariants;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
public class ProductResponse {

    private long id;

    private String name;

    private String detail;

    private String application;

    private String advantage;

    private String disadvantage;

    private String thickness;

    private Double basePrice;

    private String sku;

    private String color;

    private String size;

    private Double price;

    private Long quantity;

    private List<String> images = new ArrayList<>();
}
