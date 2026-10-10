package com.vanitha.ecommerce.service;

import com.vanitha.ecommerce.dto.ProductPatchRequest;
import com.vanitha.ecommerce.dto.ProductRequest;
import com.vanitha.ecommerce.dto.ProductResponse;
import com.vanitha.ecommerce.entity.Product;
import com.vanitha.ecommerce.exception.ProductNotFoundException;
import com.vanitha.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.nio.file.ProviderNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
       Product product = Product.builder().name(productRequest.getName()).description(productRequest.getDescription()).price(productRequest.getPrice()).stock(productRequest.getStock()).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
       Product savedProduct = productRepository.save(product);
        return mapToResponse(savedProduct);
    }

    @Override
    public ProductResponse getProductById(long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        return mapToResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found with id: "+ id));
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());
        product.setUpdatedAt(LocalDateTime.now());
        Product updatedProduct = productRepository.save(product);
        return mapToResponse(updatedProduct);
    }

    @Override
    public ProductResponse patchProduct(Long id, ProductPatchRequest request){
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found with id: "+ id));
        if (request.getName() != null) {
            product.setName(request.getName());
        }

        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }

        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }

        if (request.getStock() != null) {
            product.setStock(request.getStock());
        }
        Product updatedProduct = productRepository.save(product);
        return mapToResponse(updatedProduct);
    }
    
    @Override
    public void deleteProduct(long id) {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new ProductNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder().id(product.getId()).name(product.getName()).description(product.getDescription()).price(product.getPrice()).stock(product.getStock()).createdAt(product.getCreatedAt()).updatedAt(product.getUpdatedAt()).build();
    }
}
