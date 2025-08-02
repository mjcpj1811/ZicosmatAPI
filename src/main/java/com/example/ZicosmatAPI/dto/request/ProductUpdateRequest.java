package com.example.ZicosmatAPI.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ProductUpdateRequest {

    private long id;

    private String name;

    private String detail;

    private String application;

    private String advantage;

    private String disadvantage;

    private String thickness;

    private Double price;

    private Long quantity;

    private String sku;

    private String color;

    private String size;
}
