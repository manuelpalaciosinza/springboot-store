package com.codewithmosh.store.carts;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Basic product information used in cart items")
public class ProductDto {

    @Schema(description = "Product ID", example = "1")
    private Long id;

    @Schema(description = "Product name", example = "Wireless Mouse")
    private String name;

    @Schema(description = "Product price", example = "29.99")
    private BigDecimal price;
}
