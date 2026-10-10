package com.vanitha.ecommerce.service;

import com.vanitha.ecommerce.dto.ProductPatchRequest;
import com.vanitha.ecommerce.dto.ProductRequest;
import com.vanitha.ecommerce.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest productRequest);
    ProductResponse getProductById(long id);
    List<ProductResponse> getAllProducts();
    ProductResponse patchProduct(Long id, ProductPatchRequest request);
    ProductResponse updateProduct(Long id, ProductRequest productRequest);
    void deleteProduct(long id);
}