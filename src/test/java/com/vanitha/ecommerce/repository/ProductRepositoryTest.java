package com.vanitha.ecommerce.repository;

import com.vanitha.ecommerce.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
class ProductRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres =
            new PostgreSQLContainer("postgres:16-alpine");

    @Autowired
    private ProductRepository productRepository;

    private Product createProduct() {
        return Product.builder()
                .name("MacBook Pro")
                .description("Apple laptop")
                .price(new BigDecimal("150000.00"))
                .stock(10)
                .createdAt(java.time.LocalDateTime.now())
                .updatedAt(java.time.LocalDateTime.now())
                .build();
    }

    @Test
    void saveProduct_shouldPersistProduct() {
        Product product = createProduct();

        Product savedProduct = productRepository.save(product);

        assertNotNull(savedProduct.getId());
        assertEquals("MacBook Pro", savedProduct.getName());
        assertEquals(new BigDecimal("150000.00"), savedProduct.getPrice());
    }

    @Test
    void findById_shouldReturnProduct() {
        Product product = createProduct();

        Product savedProduct = productRepository.save(product);

        Optional<Product> result =
                productRepository.findById(savedProduct.getId());

        assertTrue(result.isPresent());
        assertEquals("MacBook Pro", result.get().getName());
        assertEquals(10, result.get().getStock());
    }

    @Test
    void findAll_shouldReturnAllProducts() {
        Product product1 = createProduct();

        Product product2 = Product.builder()
                .name("Dell XPS")
                .description("Dell laptop")
                .price(new BigDecimal("120000.00"))
                .stock(20)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        productRepository.saveAll(List.of(product1, product2));

        List<Product> products = productRepository.findAll();

        assertEquals(2, products.size());
    }

    @Test
    void updateProduct_shouldPersistUpdatedValues() {
        Product product = productRepository.save(createProduct());

        product.setName("MacBook Pro M5");
        product.setPrice(new BigDecimal("180000.00"));
        product.setStock(25);

        productRepository.save(product);

        Product updatedProduct = productRepository
                .findById(product.getId())
                .orElseThrow();

        assertEquals("MacBook Pro M5", updatedProduct.getName());
        assertEquals(new BigDecimal("180000.00"), updatedProduct.getPrice());
        assertEquals(25, updatedProduct.getStock());
    }

    @Test
    void deleteProduct_shouldRemoveProduct() {
        Product product = productRepository.save(createProduct());

        Long productId = product.getId();

        productRepository.deleteById(productId);

        Optional<Product> result =
                productRepository.findById(productId);

        assertTrue(result.isEmpty());
    }

    @Test
    void findById_shouldReturnEmptyWhenProductDoesNotExist() {
        Optional<Product> result = productRepository.findById(999L);

        assertTrue(result.isEmpty());
    }
}
