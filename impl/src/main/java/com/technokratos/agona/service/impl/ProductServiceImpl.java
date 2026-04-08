package com.technokratos.agona.service.impl;

import com.technokratos.agona.config.CacheConfig;
import com.technokratos.agona.document.ProductDocument;
import com.technokratos.agona.dto.request.ProductRequest;
import com.technokratos.agona.dto.response.ProductResponse;
import com.technokratos.agona.entity.Product;
import com.technokratos.agona.mapper.ProductMapper;
import com.technokratos.agona.repository.ProductMongoRepository;
import com.technokratos.agona.repository.ProductRepository;
import com.technokratos.agona.service.FileService;
import com.technokratos.agona.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMongoRepository productMongoRepository;
    private final ProductMapper productMapper;
    private final FileService minioService;

    @Override
    @Cacheable(value = CacheConfig.PRODUCTS_CACHE, key = "'all'")
    public List<ProductResponse> getAll() {
        log.debug("Cache MISS — loading all products from DB");
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = CacheConfig.PRODUCT_CACHE, key = "#id")
    public ProductResponse getById(UUID id) {
        log.debug("Cache MISS — loading product {} from DB", id);

        return productMongoRepository.findByProductId(id)
                .map(doc -> ProductResponse.builder()
                        .id(doc.getProductId())
                        .name(doc.getName())
                        .description(doc.getDescription())
                        .price(doc.getPrice())
                        .imageUrl(doc.getImageUrl())
                        .build())
                .orElseGet(() -> {
                    Product entity = productRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Product not found: " + id));
                    return productMapper.toResponse(entity);
                });
    }

    @Override
    @Transactional
    @Caching(
            put    = @CachePut(value = CacheConfig.PRODUCT_CACHE, key = "#result.id"),
            evict  = @CacheEvict(value = CacheConfig.PRODUCTS_CACHE, key = "'all'")
    )
    public ProductResponse create(ProductRequest request) {
        Product entity = productMapper.toEntity(request);
        entity = productRepository.save(entity);

        ProductDocument doc = buildDocument(entity);
        productMongoRepository.save(doc);

        log.debug("Product created: {}", entity.getId());
        return productMapper.toResponse(entity);
    }

    @Override
    @Transactional
    @Caching(
            put   = @CachePut(value = CacheConfig.PRODUCT_CACHE, key = "#id"),
            evict = @CacheEvict(value = CacheConfig.PRODUCTS_CACHE, key = "'all'")
    )
    public ProductResponse update(UUID id, ProductRequest request) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));

        productMapper.updateEntity(entity, request);
        entity = productRepository.save(entity);

        ProductDocument doc = productMongoRepository.findByProductId(id)
                .orElse(buildDocument(entity));
        doc.setName(entity.getName());
        doc.setDescription(entity.getDescription());
        doc.setPrice(entity.getPrice());
        doc.setUpdatedAt(LocalDateTime.now());
        productMongoRepository.save(doc);

        log.debug("Product updated: {}", id);
        return productMapper.toResponse(entity);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = CacheConfig.PRODUCT_CACHE,  key = "#id"),
            @CacheEvict(value = CacheConfig.PRODUCTS_CACHE, key = "'all'")
    })
    public void delete(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found: " + id);
        }
        productRepository.deleteById(id);
        productMongoRepository.deleteByProductId(id);
        log.debug("Product deleted: {}", id);
    }

    @Override
    @Transactional
    @Caching(
            put   = @CachePut(value = CacheConfig.PRODUCT_CACHE, key = "#id"),
            evict = @CacheEvict(value = CacheConfig.PRODUCTS_CACHE, key = "'all'")
    )
    public ProductResponse uploadImage(UUID id, MultipartFile file) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));

        String imageUrl = minioService.uploadFile(file);
        entity.setImageUrl(imageUrl);
        entity = productRepository.save(entity);

        productMongoRepository.findByProductId(id).ifPresent(doc -> {
            doc.setImageUrl(imageUrl);
            doc.setUpdatedAt(LocalDateTime.now());
            productMongoRepository.save(doc);
        });

        log.debug("Image uploaded for product {}: {}", id, imageUrl);
        return productMapper.toResponse(entity);
    }

    private ProductDocument buildDocument(Product entity) {
        return ProductDocument.builder()
                .productId(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .imageUrl(entity.getImageUrl())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
