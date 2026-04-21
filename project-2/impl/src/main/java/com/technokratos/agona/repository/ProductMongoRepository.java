package com.technokratos.agona.repository;

import com.technokratos.agona.document.ProductDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductMongoRepository extends MongoRepository<ProductDocument, String> {

    Optional<ProductDocument> findByProductId(UUID productId);

    void deleteByProductId(UUID productId);
}
