package com.ankit.learn_springboot.ProductModule.productDto;

import com.ankit.learn_springboot.ProductModule.productEntity.Category;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseProductDto {
    private Integer id;
    private String name;
    private String description;
    private Integer stockQuantity;
    private List<Category> category;
    private BigDecimal price;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
