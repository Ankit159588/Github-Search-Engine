package com.ankit.learn_springboot.ProductModule.productService;

import com.ankit.learn_springboot.ProductModule.productDto.RequestProductDto;
import com.ankit.learn_springboot.ProductModule.productDto.ResponseProductDto;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ProductService {
    Page<ResponseProductDto> getAllProducts(Pageable pageable);

    ResponseProductDto getProductById(Integer id);

    ResponseProductDto createNewProduct(RequestProductDto requestProductDto);

    void deleteAproduct(Integer id);

    ResponseProductDto updateProduct(Integer id, RequestProductDto requestProductDto);

    ResponseProductDto partialUpdateProduct(Integer id, Map<String, Object> fields);
}
