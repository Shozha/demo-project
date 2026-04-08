package com.technokratos.agona.mapper;

import com.technokratos.agona.document.ProductDocument;
import com.technokratos.agona.dto.request.ProductRequest;
import com.technokratos.agona.dto.response.ProductResponse;
import com.technokratos.agona.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductRequest request);

    ProductResponse toResponse(Product entity);

    void updateEntity(@MappingTarget Product entity, ProductRequest request);

    ProductDocument toDocument(Product entity);
}
