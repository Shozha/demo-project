package com.technokratos.agona.exception.product;

import com.technokratos.agona.exception.NotFoundException;

import java.util.UUID;

public class ProductNotFoundException extends NotFoundException {

    public ProductNotFoundException(UUID id) {
        super(String.format("Product with id '%s' not found", id));
    }
}
