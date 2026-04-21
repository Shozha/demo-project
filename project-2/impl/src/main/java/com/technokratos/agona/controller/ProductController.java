package com.technokratos.agona.controller;

import com.technokratos.agona.api.ProductApi;
import com.technokratos.agona.dto.request.ProductRequest;
import com.technokratos.agona.dto.response.ProductResponse;
import com.technokratos.agona.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductApi {

    private final ProductService productService;

    @Override
    public ResponseEntity<List<ProductResponse>> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }

    @Override
    public ResponseEntity<ProductResponse> getById(UUID id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @Override
    public ResponseEntity<ProductResponse> create(ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.create(request));
    }

    @Override
    public ResponseEntity<ProductResponse> update(UUID id, ProductRequest request) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    @Override
    public ResponseEntity<Void> delete(UUID id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ProductResponse> uploadImage(UUID id, MultipartFile file) {
        return ResponseEntity.ok(productService.uploadImage(id, file));
    }
}
