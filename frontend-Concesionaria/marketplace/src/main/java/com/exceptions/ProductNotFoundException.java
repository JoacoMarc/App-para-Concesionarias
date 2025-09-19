package com.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "El producto no existe")
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException() {
        super("El producto no existe");
    }

    public ProductNotFoundException(String message) {
        super(message);
    }
}
