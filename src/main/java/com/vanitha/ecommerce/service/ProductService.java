package com.vanitha.ecommerce.service;

import com.vanitha.ecommerce.dto.PageResponse;
import com.vanitha.ecommerce.dto.ProductPatchRequest;
import com.vanitha.ecommerce.dto.ProductRequest;
import com.vanitha.ecommerce.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest productRequest);
    ProductResponse getProductById(long id);
    List<ProductResponse> getAllProducts();
    PageResponse<ProductResponse> getProducts(Pageable pageable);
    ProductResponse patchProduct(Long id, ProductPatchRequest request);
    ProductResponse updateProduct(Long id, ProductRequest productRequest);
    void deleteProduct(long id);
}