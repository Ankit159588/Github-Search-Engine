package com.ankit.learn_springboot.ProductModule.productEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products")
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer id;

    @NotBlank
    @Column(nullable = false, name = "name")

    private String name;

    @Size(max = 500)
    @Column(length = 500, name = "description")
    private String description;

    @Min(0)
    @Column(name = "stock_quantity",nullable = false)
    private Integer stockQuantity;

    @ElementCollection
    @CollectionTable(
            name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id")
    )
    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private List<Category> category;

    @URL
    @Column(name = "image_url")
    private String imageUrl;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Min(0)
    @Column(nullable = false)
    private BigDecimal price;

}
    