package com.vanitha.ecommerce;

import com.vanitha.ecommerce.dto.ProductRequest;
import com.vanitha.ecommerce.dto.ProductResponse;
import com.vanitha.ecommerce.entity.Product;
import com.vanitha.ecommerce.exception.ProductNotFoundException;
import com.vanitha.ecommerce.repository.ProductRepository;
import com.vanitha.ecommerce.service.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void createProduct_shouldReturnCreatedProduct(){
        ProductRequest productRequest = ProductRequest.builder().name("Macbook Pro").description("Apple laptop").price(new BigDecimal("150000")).stock(10).build();
        Product savedProduct = Product.builder()
                .id(1L)
                .name("MacBook Pro")
                .description("Apple laptop")
                .price(new BigDecimal("150000"))
                .stock(10)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct); // I don't care exactly which Product object is passed. If a Product is passed, return savedProduct.

        ProductResponse productResponse = productService.createProduct(productRequest);
        assertEquals(1L, productResponse.getId());
        assertEquals("MacBook Pro", productResponse.getName());
        assertEquals("Apple laptop", productResponse.getDescription());
        assertEquals(new BigDecimal("150000"), productResponse.getPrice());
        assertEquals(10, productResponse.getStock());

        verify(productRepository).save(any(Product.class));
    }

    @Test
    void getProductById_shouldThrowExceptionWhenProductDoesNotExist(){
        when(productRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(999L));
        verify(productRepository).findById(999L);
        //when(productRepository.findById(999L)).thenThrow(new RuntimeException("Database error"));
        //verifyNoInteractions(productRepository);
    }

    @Test
    void getProductById_shouldReturnProduct(){
        Product product = Product.builder()
                .id(1L)
                .name("Macbook Pro")
                .description("Apple laptop")
                .price(new BigDecimal("150000"))
                .stock(10)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product)); //When findById(1L) is called, then return this product.
        ProductResponse productResponse = productService.getProductById(1L);
        assertEquals(1L, productResponse.getId());
        assertEquals("Macbook Pro", productResponse.getName());

        verify(productRepository).findById(1L); // Verify that findById(1L) was called.
    }

    @Test
    void getProductById_shouldThrowProductNotFoundException(){
        Product product = Product.builder()
                .id(1L)
                .name("Macbook Pro")
                .description("Apple laptop")
                .price(new BigDecimal("150000"))
                .stock(10)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
        verify(productRepository).findById(1L);
    }

    @Test
    void getAllProducts_shouldReturnAllProducts(){
        Product product1 = Product.builder()
                .id(1L)
                .name("MacBook Pro")
                .price(new BigDecimal("150000"))
                .stock(10)
                .build();
        Product product2 = Product.builder()
                .id(2L)
                .name("iPhone")
                .price(new BigDecimal("80000"))
                .stock(20)
                .build();
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        List<ProductResponse> response = productService.getAllProducts();

        assertEquals(2, response.size());
        assertEquals("MacBook Pro", response.get(0).getName());
        assertEquals(2L, response.get(1).getId());

        verify(productRepository).findAll();
    }

    @Test
    void deleteProduct_shouldDeleteProduct(){
        Long productId = 1L;
        Product product = new Product();
        product.setId(1L);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        productService.deleteProduct(productId);
        verify(productRepository).delete(product);
    }
}
