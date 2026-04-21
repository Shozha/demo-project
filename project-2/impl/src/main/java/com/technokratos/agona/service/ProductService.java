package com.technokratos.agona.service;

import com.technokratos.agona.dto.request.ProductRequest;
import com.technokratos.agona.dto.response.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    List<ProductResponse> getAll();

    ProductResponse getById(UUID id);

    ProductResponse create(ProductRequest request);

    ProductResponse update(UUID id, ProductRequest request);

    void delete(UUID id);

    ProductResponse uploadImage(UUID id, MultipartFile file);

}
