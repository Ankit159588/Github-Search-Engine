package com.ankit.learn_springboot.ProductModule.productService.productServiceImpl;

import com.ankit.learn_springboot.ProductModule.excptation.ProductNotFoundException;
import com.ankit.learn_springboot.ProductModule.productDto.RequestProductDto;
import com.ankit.learn_springboot.ProductModule.productDto.ResponseProductDto;
import com.ankit.learn_springboot.ProductModule.productEntity.Category;
import com.ankit.learn_springboot.ProductModule.productEntity.Product;
import com.ankit.learn_springboot.ProductModule.productRepository.ProductRepository;
import com.ankit.learn_springboot.ProductModule.productService.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Page<ResponseProductDto> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);

        return products.map(product -> new ResponseProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getStockQuantity(),
                product.getCategory(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        ));
    }

    @Override
    public ResponseProductDto getProductById(Integer id) {

        Product product = productRepository.findById(id)
                .orElseThrow(
                        () -> new ProductNotFoundException(id)
                );
        ResponseProductDto responseProductDto = new ResponseProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getStockQuantity(),
                product.getCategory(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );

        return responseProductDto;
    }

    @Override
    public ResponseProductDto createNewProduct(RequestProductDto requestProductDto) {
        Product product = Product.builder()
                .name(requestProductDto.getName())
                .description(requestProductDto.getDescription())
                .stockQuantity(requestProductDto.getStockQuantity())
                .category(requestProductDto.getCategory())
                .imageUrl(requestProductDto.getImageUrl())
                .price(requestProductDto.getPrice())
                .build();

        Product savedProduct = productRepository.save(product);

        // Step 3 - convert saved Product → ResponseDto and return
        ResponseProductDto responseProductDto = new ResponseProductDto(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getDescription(),
                savedProduct.getStockQuantity(),
                savedProduct.getCategory(),
                product.getPrice(),
                savedProduct.getImageUrl(),
                savedProduct.getCreatedAt(),
                savedProduct.getUpdatedAt()
        );
        return responseProductDto;
    }

    @Override
    public void deleteAproduct(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(id));

        productRepository.delete(product);
    }

    @Override
    public ResponseProductDto updateProduct(Integer id, RequestProductDto requestProductDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.setName(requestProductDto.getName());
        product.setDescription(requestProductDto.getDescription());
        product.setStockQuantity(requestProductDto.getStockQuantity());
        product.setCategory(requestProductDto.getCategory());
        product.setPrice(requestProductDto.getPrice());
        product.setImageUrl(requestProductDto.getImageUrl());

        Product updatedProduct = productRepository.save(product);
        // conversion of product to dto response
        return mapToDto(updatedProduct);
    }

    @Override
    public ResponseProductDto partialUpdateProduct(Integer id, Map<String, Object> fields) {
        Product product =    productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        if(fields.containsKey("name")){
            product.setName((String) fields.get("name"));
        }
        if (fields.containsKey("description")) {
            product.setDescription((String) fields.get("description"));
        }
        if (fields.containsKey("stockQuantity")) {
            product.setStockQuantity((Integer) fields.get("stockQuantity"));
        }
        if (fields.containsKey("category")) {
            List<String> categoryStrings = (List<String>) fields.get("category");
            List<Category> categories = categoryStrings.stream()
                    .map(Category::valueOf)  // converts "SMARTPHONE" → Category.SMARTPHONE
                    .toList();
            product.setCategory(categories);
        }
        if(fields.containsKey("price")){
            product.setPrice((BigDecimal) fields.get("price"));
        }
        if (fields.containsKey("imageUrl")) {
            product.setImageUrl((String) fields.get("imageUrl"));
        }
        Product updatedProduct = productRepository.save(product);
        return mapToDto(updatedProduct);
    }

    private ResponseProductDto mapToDto(Product product) {
        return ResponseProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .stockQuantity(product.getStockQuantity())
                .category(product.getCategory())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

//    private ResponseProductDto mapToDto(Product product) {
//        return new ResponseProductDto(
//                product.getId(),
//                product.getName(),
//                product.getDescription(),
//                product.getStockQuantity(),
//                product.getCategory(),
//                product.getImageUrl(),
//                product.getCreatedAt(),
//                product.getUpdatedAt()
//        );
//    }

}
