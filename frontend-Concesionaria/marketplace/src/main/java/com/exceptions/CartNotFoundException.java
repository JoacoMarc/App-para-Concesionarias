package com.exceptions;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException() {
        super("Cart not found");
    }

    public CartNotFoundException(String message) {
        super(message);
    }
}
