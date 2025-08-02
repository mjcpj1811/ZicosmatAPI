package com.example.ZicosmatAPI.dto.request;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductCreateRequest {

    private String name;

    private String detail;

    private String application;

    private String advantage;

    private String disadvantage;

    private String thickness;

    private Double basePrice;

    private Double price;

    private Long quantity;

    private String sku;

    private String color;

    private String size;
}
