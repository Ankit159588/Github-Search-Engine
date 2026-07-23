package com.ankit.learn_springboot.ProductModule.productDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.util.List;

public class UpdateProductDto {
    private String name;
    @Size(max = 500)
    private String description;
    @Min(0)
    private Integer stockQuantity;
    private List<String> category;
    private String imageUrl;
}
