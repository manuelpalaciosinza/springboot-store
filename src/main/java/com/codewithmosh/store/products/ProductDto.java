package com.codewithmosh.store.products;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Byte categoryId;
}
