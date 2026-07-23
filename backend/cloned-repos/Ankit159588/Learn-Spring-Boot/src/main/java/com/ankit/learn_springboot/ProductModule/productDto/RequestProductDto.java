package com.ankit.learn_springboot.ProductModule.productDto;

import com.ankit.learn_springboot.ProductModule.productEntity.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class RequestProductDto {

    @NotBlank
    private String name;

    @Size(max = 500)
    private String description;

    @Min(0)
    private Integer stockQuantity;

    @NotEmpty
    private List<Category> category;

    private String imageUrl;

    @Min(0)
    private BigDecimal price;
}