package com.vanitha.ecommerce.exception;

public class ProductNotFoundException extends RuntimeException{
    private String message;

    public ProductNotFoundException(String message) {
        super(message);
    }
}
