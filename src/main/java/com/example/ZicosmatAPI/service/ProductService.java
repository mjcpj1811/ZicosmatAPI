package com.example.ZicosmatAPI.service;

import com.example.ZicosmatAPI.dto.request.ProductCreateRequest;
import com.example.ZicosmatAPI.dto.request.ProductUpdateRequest;
import com.example.ZicosmatAPI.dto.response.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    ProductResponse getProductById(long id);

    ProductResponse getProductDetail(String sku);

    List<ProductResponse> getAllProduct();

    long addProduct(ProductCreateRequest request, MultipartFile[] images) throws IOException;

    void updateProduct(ProductUpdateRequest request);

    void updateImages(long id, MultipartFile[] images);

    void deleteProduct(long id);
}
