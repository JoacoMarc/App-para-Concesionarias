package com.exceptions;

public class CartDuplicateException extends RuntimeException {
    public CartDuplicateException() {
        super("Cart already exists");
    }

    public CartDuplicateException(String message) {
        super(message);
    }
}
