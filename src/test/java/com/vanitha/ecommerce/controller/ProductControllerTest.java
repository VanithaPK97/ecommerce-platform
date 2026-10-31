package com.vanitha.ecommerce.controller;

import com.vanitha.ecommerce.dto.ProductRequest;
import com.vanitha.ecommerce.dto.ProductResponse;

import com.vanitha.ecommerce.exception.ProductNotFoundException;
import com.vanitha.ecommerce.service.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @MockitoBean
    private ProductServiceImpl productService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createProduct_shouldReturnCreatedProduct() throws Exception {

        ProductRequest request = ProductRequest.builder()
                .name("Macbook Pro")
                .description("Apple laptop")
                .price(new BigDecimal("150000"))
                .stock(30)
                .build();

        ProductResponse response = ProductResponse.builder()
                .id(1L)
                .name("Macbook Pro")
                .description("Apple laptop")
                .price(new BigDecimal("150000"))
                .stock(30)
                .build();

        when(productService.createProduct(any(ProductRequest.class))).thenReturn(response);
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Macbook Pro"))
                .andExpect(jsonPath("$.stock").value(30));

        verify(productService).createProduct(any(ProductRequest.class));
    }

    @Test
    void createProduct_shouldReturnBadRequestWhenNameIsEmpty() throws Exception {

        ProductRequest request = ProductRequest.builder()
                .name("")
                .description("Apple laptop")
                .price(new BigDecimal("150000"))
                .stock(30)
                .build();

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_FAILED"))
                .andExpect(jsonPath("$.fieldErrors.name")
                        .value("Product name is required"));

        verifyNoInteractions(productService);
    }

    @Test
    void getProductById_shouldReturnProduct() throws Exception {

        ProductResponse response = ProductResponse.builder()
                .id(1L)
                .name("Macbook Pro")
                .description("Apple laptop")
                .price(new BigDecimal("150000"))
                .stock(30)
                .build();

        when(productService.getProductById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Macbook Pro"))
                .andExpect(jsonPath("$.price").value(150000))
                .andExpect(jsonPath("$.stock").value(30));

        verify(productService).getProductById(1L);
    }

    @Test
    void getAllProducts_shouldReturnProductList() throws Exception {

        ProductResponse product1 = ProductResponse.builder()
                .id(1L)
                .name("Macbook Pro")
                .description("Apple laptop")
                .price(new BigDecimal("150000"))
                .stock(30)
                .build();

        ProductResponse product2 = ProductResponse.builder()
                .id(2L)
                .name("Dell XPS")
                .description("Dell laptop")
                .price(new BigDecimal("120000"))
                .stock(20)
                .build();

        when(productService.getAllProducts())
                .thenReturn(List.of(product1, product2));

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Macbook Pro"))
                .andExpect(jsonPath("$[1].name").value("Dell XPS"));

        verify(productService).getAllProducts();
    }

    @Test
    void updateProduct_shouldReturnUpdatedProduct() throws Exception {

        ProductRequest request = ProductRequest.builder()
                .name("Macbook Pro M5")
                .description("Updated Apple laptop")
                .price(new BigDecimal("180000"))
                .stock(25)
                .build();

        ProductResponse response = ProductResponse.builder()
                .id(1L)
                .name("Macbook Pro M5")
                .description("Updated Apple laptop")
                .price(new BigDecimal("180000"))
                .stock(25)
                .build();

        when(productService.updateProduct(eq(1L), any(ProductRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Macbook Pro M5"))
                .andExpect(jsonPath("$.price").value(180000))
                .andExpect(jsonPath("$.stock").value(25));

        verify(productService).updateProduct(eq(1L), any(ProductRequest.class));
    }

    @Test
    void deleteProduct_shouldReturnNoContent() throws Exception {

        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/v1/products/1"))
                .andExpect(status().isNoContent());

        verify(productService).deleteProduct(1L);
    }

    @Test
    void getProductById_shouldReturnNotFoundWhenProductDoesNotExist()
            throws Exception {

        when(productService.getProductById(999L))
                .thenThrow(new ProductNotFoundException("Product not found with id: 999"));

        mockMvc.perform(get("/api/v1/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("PRODUCT_NOT_FOUND"))
                .andExpect(jsonPath("$.message")
                        .value("Product not found with id: 999"));

        verify(productService).getProductById(999L);
    }
}
