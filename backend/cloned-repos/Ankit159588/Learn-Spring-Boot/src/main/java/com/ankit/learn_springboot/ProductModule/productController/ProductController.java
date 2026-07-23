package com.ankit.learn_springboot.ProductModule.productController;

import com.ankit.learn_springboot.ProductModule.productDto.RequestProductDto;
import com.ankit.learn_springboot.ProductModule.productDto.ResponseProductDto;
import com.ankit.learn_springboot.ProductModule.productEntity.Category;
import com.ankit.learn_springboot.ProductModule.productService.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<Page<ResponseProductDto>> getAllProducts(
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ResponseProductDto> getProductById(@PathVariable Integer id){
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping("/products")
    public ResponseEntity<ResponseProductDto> addProduct( @Valid @RequestBody RequestProductDto requestProductDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(
            productService.createNewProduct(requestProductDto)
        );
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id){
        productService.deleteAproduct(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ResponseProductDto> updateProduct(@PathVariable Integer id,
                                                            @Valid @RequestBody RequestProductDto requestProductDto){
        return ResponseEntity.ok(productService.updateProduct(id, requestProductDto));
    }

    @PatchMapping("/products/{id}")
    public ResponseEntity<ResponseProductDto> partialUpdateProduct(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> fields) {   // Map because we don't know which fields are coming
        return ResponseEntity.ok(productService.partialUpdateProduct(id, fields));
    }


    // category get mapping
    @GetMapping("/categories")
    public ResponseEntity<Category[]> getCategories() {
        return ResponseEntity.ok(Category.values());
    }

}
