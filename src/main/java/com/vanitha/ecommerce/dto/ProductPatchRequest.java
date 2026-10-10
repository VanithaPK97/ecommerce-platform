package com.vanitha.ecommerce.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPatchRequest {

    private String name;

    private String description;

    private BigDecimal price;

    private Integer stock;
}