package com.ankit.learn_springboot.ProductModule.excptation;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(Integer id) {
        super("Product not found with id " + id);
    }
}
