package com.technokratos.agona.api;

import com.technokratos.agona.dto.request.ProductRequest;
import com.technokratos.agona.dto.response.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/products")
public interface ProductApi {

    @GetMapping
    ResponseEntity<List<ProductResponse>> getAll();

    @GetMapping("/{id}")
    ResponseEntity<ProductResponse> getById(@PathVariable UUID id);

    @PostMapping
    ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request);

    @PutMapping("/{id}")
    ResponseEntity<ProductResponse> update(@PathVariable UUID id,
                                           @Valid @RequestBody ProductRequest request);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id);

    @PostMapping("/{id}/image")
    ResponseEntity<ProductResponse> uploadImage(@PathVariable UUID id,
                                                @RequestParam("file") MultipartFile file);
}
