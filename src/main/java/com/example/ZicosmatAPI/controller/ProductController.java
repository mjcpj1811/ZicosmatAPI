package com.example.ZicosmatAPI.controller;

import com.example.ZicosmatAPI.dto.request.ProductCreateRequest;
import com.example.ZicosmatAPI.dto.request.ProductUpdateRequest;
import com.example.ZicosmatAPI.dto.response.ApiResponse;
import com.example.ZicosmatAPI.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j(topic = "PRODUCT-CONTROLLER")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Get all product", description = "Get all product")
    @GetMapping("/all")
    public ApiResponse getAllProduct() throws IOException {
        log.info("Get all product");

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Get all product successfully")
                .data(productService.getAllProduct()).build();
    }

    @Operation(summary = "Get product by id", description = "Get product by Id")
    @GetMapping("/{id}")
    public ApiResponse getProductById(@PathVariable long id) throws IOException {
        log.info("Get product by id");

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Get product detail successfully")
                .data(productService.getProductById(id)).build();
    }


    @Operation(summary = "Create product", description = "Create new product")
    @PostMapping(value = "/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse addProduct(@ModelAttribute ProductCreateRequest request,
                                  @RequestParam MultipartFile[] images) throws IOException {
        log.info("Add product");

        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Created product successfully")
                .data(productService.addProduct(request,images)).build();
    }

    @Operation(summary = "Get product detail", description = "Get product detail")
    @GetMapping("/sku/{sku}")
    public ApiResponse getProduct(@PathVariable String sku) {
        log.info("Get product");

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Get product detail successfully")
                .data(productService.getProductDetail(sku)).build();
    }

    @Operation(summary = "Update product", description = "Update product")
    @PutMapping("/update")
    public ApiResponse updateProduct(@RequestBody ProductUpdateRequest request) {
        log.info("Update product");

        productService.updateProduct(request);

        return ApiResponse.builder()
                .status(HttpStatus.ACCEPTED.value())
                .message("Updated product successfully")
                .data("").build();
    }

    @Operation(summary = "Delete product", description = "Delete product")
    @DeleteMapping("/del/{id}")
    public ApiResponse deleteProduct(@PathVariable long id) {
        log.info("Delete product");

        productService.deleteProduct(id);

        return ApiResponse.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Delete product successfully")
                .data("").build();
    }
}
