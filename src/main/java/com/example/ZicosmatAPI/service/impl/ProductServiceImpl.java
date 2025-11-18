package com.example.ZicosmatAPI.service.impl;

import com.example.ZicosmatAPI.dto.request.ProductCreateRequest;
import com.example.ZicosmatAPI.dto.request.ProductUpdateRequest;
import com.example.ZicosmatAPI.dto.response.ProductResponse;
import com.example.ZicosmatAPI.exception.DuplicateResourceException;
import com.example.ZicosmatAPI.exception.ResourceNotFoundException;
import com.example.ZicosmatAPI.model.Image;
import com.example.ZicosmatAPI.model.Product;
import com.example.ZicosmatAPI.model.ProductVariants;
import com.example.ZicosmatAPI.repository.ImageRepository;
import com.example.ZicosmatAPI.repository.ProductRepository;
import com.example.ZicosmatAPI.repository.ProductVariantRepository;
import com.example.ZicosmatAPI.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "PRODUCT-SERVICE")
public class ProductServiceImpl implements ProductService {


    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final ProductVariantRepository productVariantRepository;

    @Override
    public ProductResponse getProductById(long id) {
        log.info("getProductById");

        ProductVariants productVariants = productVariantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Product product = productVariants.getProduct();
        List<String> images = new ArrayList<>();
        productVariants.getImages().forEach(image -> {
            images.add(image.getImageUrl());
        });

        return ProductResponse.builder()
                .id(productVariants.getId())
                .name(product.getName())
                .detail(product.getDetail())
                .application(product.getApplication())
                .advantage(product.getAdvantage())
                .disadvantage(product.getDisadvantage())
                .thickness(product.getThickness())
                .basePrice(product.getBasePrice())
                .sku(productVariants.getSku())
                .color(productVariants.getColor())
                .size(productVariants.getSize())
                .price(productVariants.getPrice())
                .quantity(productVariants.getQuantity())
                .images(images)
                .build();
    }

    @Override
    public ProductResponse getProductDetail(String sku) {
        log.info("Get product detail");

        ProductVariants productVariants = productVariantRepository.findBySku(sku);
        Product product = productVariants.getProduct();

        List<String> images = new ArrayList<>();
        productVariants.getImages().forEach(image -> {
            images.add(image.getImageUrl());
        });

        return ProductResponse.builder()
                .id(productVariants.getId())
                .name(product.getName())
                .detail(product.getDetail())
                .application(product.getApplication())
                .advantage(product.getAdvantage())
                .disadvantage(product.getDisadvantage())
                .thickness(product.getThickness())
                .basePrice(product.getBasePrice())
                .sku(sku)
                .color(productVariants.getColor())
                .size(productVariants.getSize())
                .price(productVariants.getPrice())
                .quantity(productVariants.getQuantity())
                .images(images)
                .build();
    }

    @Override
    public List<ProductResponse> getAllProduct() {
        log.info("Get all products");

        List<ProductVariants> productVariants = productVariantRepository.findAll();

        return productVariants.stream().map(productVariant -> {
            Product product = productVariant.getProduct();
            List<String> images = new ArrayList<>();

            productVariant.getImages().forEach(image -> {
                images.add(image.getImageUrl());
            });

            return ProductResponse.builder()
                    .id(productVariant.getId())
                    .name(product.getName())
                    .detail(product.getDetail())
                    .application(product.getApplication())
                    .advantage(product.getAdvantage())
                    .disadvantage(product.getDisadvantage())
                    .thickness(product.getThickness())
                    .basePrice(product.getBasePrice())
                    .sku(productVariant.getSku())
                    .color(productVariant.getColor())
                    .size(productVariant.getSize())
                    .price(productVariant.getPrice())
                    .quantity(productVariant.getQuantity())
                    .images(images)
                    .build();
        }).toList();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public long addProduct(ProductCreateRequest request, MultipartFile[] files) throws IOException {
        log.info("Adding product");

        Product product = productRepository.findProductByName(request.getName());

        if(product != null) {
            ProductVariants productVariants = productVariantRepository.findBySku(request.getSku());

            if(productVariants != null) {
                throw new DuplicateResourceException("Sku already exists");
            }

            ProductVariants productVariant = new ProductVariants();
            productVariant.setSku(request.getSku());
            productVariant.setPrice(request.getPrice());
            productVariant.setSize(request.getSize());
            productVariant.setColor(request.getColor());
            productVariant.setQuantity(request.getQuantity());
            productVariant.setCreatedAt(LocalDateTime.now());
            productVariant.setUpdatedAt(LocalDateTime.now());
            productVariant.setProduct(product);
            productVariantRepository.save(productVariant);

            List<Image> images = new ArrayList<>();
            Path dir = Paths.get("uploads/", "product-" + productVariants.getId());
            Files.createDirectories(dir);

            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    Path filePath = dir.resolve(fileName);
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                    Image image = new Image();
                    image.setProduct(product);
                    image.setImageUrl("/uploads/product-" + productVariants.getId() + "/" + fileName);
                    image.setVariant(productVariant);
                    image.setCreatedAt(LocalDateTime.now());
                    image.setUpdatedAt(LocalDateTime.now());
                    images.add(image);
                }
            }

            productVariant.setImages(images);
            productVariantRepository.save(productVariant);

            return productVariant.getId();
        } else {
            Product productNew = new Product();
            productNew.setName(request.getName());
            productNew.setDetail(request.getDetail());
            productNew.setApplication(request.getApplication());
            productNew.setAdvantage(request.getAdvantage());
            productNew.setDisadvantage(request.getDisadvantage());
            productNew.setBasePrice(request.getBasePrice());
            productNew.setThickness(request.getThickness());
            productNew.setCreatedAt(LocalDateTime.now());
            productNew.setUpdatedAt(LocalDateTime.now());
            productRepository.save(productNew);
            log.info(productNew.toString());

            ProductVariants productVariants = new ProductVariants();
            productVariants.setSku(request.getSku());
            productVariants.setPrice(request.getPrice());
            productVariants.setSize(request.getSize());
            productVariants.setColor(request.getColor());
            productVariants.setQuantity(request.getQuantity());
            productVariants.setCreatedAt(LocalDateTime.now());
            productVariants.setUpdatedAt(LocalDateTime.now());
            productVariants.setProduct(productNew);
            productVariantRepository.save(productVariants);

            List<Image> images = new ArrayList<>();
            Path dir = Paths.get("uploads/", "product-" + productVariants.getId());
            Files.createDirectories(dir);

            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    Path filePath = dir.resolve(fileName);
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                    Image image = new Image();
                    image.setProduct(productNew);
                    image.setImageUrl("/uploads/product-" + productVariants.getId() + "/" + fileName);
                    image.setUpdatedAt(LocalDateTime.now());
                    image.setCreatedAt(LocalDateTime.now());
                    image.setVariant(productVariants);
                    images.add(image);
                }
            }

            productVariants.setImages(images);
            productVariantRepository.save(productVariants);

            log.info(productVariants.toString());

            return productVariants.getId();
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateProduct(ProductUpdateRequest request) {
        log.info("Updating product");

        ProductVariants productVariants = productVariantRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Product product = productRepository.findProductByNameAndThickness(request.getName(), request.getThickness());

        if(product != null) {
            product.setDetail(request.getDetail());
            product.setApplication(request.getApplication());
            product.setAdvantage(request.getAdvantage());
            product.setDisadvantage(request.getDisadvantage());
            product.setUpdatedAt(LocalDateTime.now());
            productRepository.save(product);
        } else {
            Product productNew = new Product();
            productNew.setName(request.getName());
            productNew.setDetail(request.getDetail());
            productNew.setApplication(request.getApplication());
            productNew.setAdvantage(request.getAdvantage());
            productNew.setDisadvantage(request.getDisadvantage());
            productNew.setThickness(request.getThickness());
            productNew.setCreatedAt(LocalDateTime.now());
            productNew.setUpdatedAt(LocalDateTime.now());
            productRepository.save(productNew);
        }

        productVariants.setProduct(product);
        productVariants.setPrice(request.getPrice());
        productVariants.setSize(request.getSize());
        productVariants.setColor(request.getColor());
        productVariants.setQuantity(request.getQuantity());
        productVariants.setCreatedAt(LocalDateTime.now());
        productVariants.setUpdatedAt(LocalDateTime.now());
        productVariantRepository.save(productVariants);

    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateImages(long id, MultipartFile[] images, String[] imagesToDelete) throws IOException {
        log.info("Updating images for product variant ID: {}", id);

        ProductVariants productVariants = productVariantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        List<Image> updatedImages = new ArrayList<>();
        Path dir = Paths.get("uploads/", "product-" + productVariants.getId());
        Files.createDirectories(dir);

        // Xử lý xóa ảnh
        if (imagesToDelete != null && imagesToDelete.length > 0) {
            List<Image> existingImages = productVariants.getImages() != null ? new ArrayList<>(productVariants.getImages()) : new ArrayList<>();

            for (String imageUrlToDelete : imagesToDelete) {
                // Làm sạch chuỗi
                String cleanedImageUrl = imageUrlToDelete
                        .replace("[", "")
                        .replace("]", "")
                        .replace("\"", "")
                        .trim();
                log.info("Processing image URL for deletion: {}", cleanedImageUrl);

                // Tìm ảnh trong DB
                Image image = imageRepository.findByImageUrl(cleanedImageUrl);
                if (image == null) {
                    log.warn("No image found in DB for URL: {}. Skipping deletion.", cleanedImageUrl);
                    continue; // Bỏ qua nếu không tìm thấy
                }

                // Xây dựng đường dẫn file
                Path filePathToDelete = dir.resolve(cleanedImageUrl.replace("/uploads/product-" + productVariants.getId() + "/", ""));
                log.info("Attempting to delete file at: {}", filePathToDelete);

                try {
                    if (Files.exists(filePathToDelete)) {
                        Files.deleteIfExists(filePathToDelete);
                        log.info("Successfully deleted image file: {}", filePathToDelete);
                    } else {
                        log.warn("Image file does not exist at: {}. Proceeding to remove from DB.", filePathToDelete);
                    }

                    // Xóa ảnh khỏi DB
                    imageRepository.delete(image);
                    existingImages.remove(image); // Cập nhật danh sách existingImages
                    log.info("Successfully deleted image from DB: {}", cleanedImageUrl);

                } catch (IOException e) {
                    log.error("Failed to delete image file {}: {}", filePathToDelete, e.getMessage());
                    throw new RuntimeException("Failed to delete image file: " + filePathToDelete, e);
                }
            }

            // Cập nhật và lưu vào DB
            productVariants.setImages(existingImages);
            ProductVariants updatedProductVariants = productVariantRepository.save(productVariants);
            log.info("Successfully updated product variants with remaining images. ID: {}, Images count: {}",
                    updatedProductVariants.getId(), updatedProductVariants.getImages().size());
        }

        // Xử lý thêm ảnh mới
        if (images != null && images.length > 0) {
            List<Image> imagesNew = new ArrayList<>();
            for (MultipartFile image : images) {
                if (image.isEmpty()) {
                    log.warn("Empty file skipped: {}", image.getOriginalFilename());
                    continue;
                }

                try {
                    String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
                    Path filePath = dir.resolve(fileName);
                    Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                    Image imageNew = new Image();
                    imageNew.setProduct(productVariants.getProduct());
                    imageNew.setImageUrl("/uploads/product-" + productVariants.getId() + "/" + fileName);
                    imageNew.setUpdatedAt(LocalDateTime.now());
                    imageNew.setCreatedAt(LocalDateTime.now());
                    imageNew.setVariant(productVariants);
                    imagesNew.add(imageNew);
                } catch (IOException e) {
                    log.error("Error uploading image: {}", e.getMessage());
                    throw new RuntimeException("Failed to upload image: " + e.getMessage());
                }
            }

            // Kết hợp ảnh còn lại và ảnh mới
            List<Image> existingImages = productVariants.getImages() != null ? new ArrayList<>(productVariants.getImages()) : new ArrayList<>();
            existingImages.addAll(imagesNew);
            productVariants.setImages(existingImages);
            productVariantRepository.save(productVariants);
        } else {
            log.warn("No new images provided for update");
        }

        log.info("Images updated successfully for product variant ID: {}", id);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteProduct(long id) {
        log.info("Deleting product with ID: {}", id);

        ProductVariants productVariants = productVariantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (productVariants.getImages() != null && !productVariants.getImages().isEmpty()) {
            for (Image oldImage : new ArrayList<>(productVariants.getImages())) {
                Path oldFilePath = Paths.get("uploads/", "product-" + productVariants.getId(),
                        oldImage.getImageUrl().replace("/uploads/product-" + productVariants.getId() + "/", ""));
                log.info("Attempting to delete image file: {}", oldFilePath);

                try {
                    if (Files.exists(oldFilePath)) {
                        Files.deleteIfExists(oldFilePath);
                        log.info("Successfully deleted image file: {}", oldFilePath);
                    } else {
                        log.warn("Image file does not exist at: {}. Skipping deletion.", oldFilePath);
                    }
                    imageRepository.delete(oldImage); // Xóa bản ghi ảnh khỏi DB
                } catch (IOException e) {
                    log.warn("Failed to delete image file {} due to IO error: {}", oldFilePath, e.getMessage());
                    // Tiếp tục xóa bản ghi DB ngay cả khi file không xóa được
                    imageRepository.delete(oldImage);
                }
            }
            productVariants.setImages(new ArrayList<>());
        }

        // Xóa ProductVariants
        productVariantRepository.delete(productVariants);
        log.info("Product with ID {} deleted successfully", id);
    }
}
