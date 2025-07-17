package com.codewithmosh.store.carts;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Represents an item in the shopping cart")
public class CartItemDto {

    @Schema(description = "Product information")
    private ProductDto product;

    @Schema(description = "Quantity of the product", example = "2")
    private int quantity;

    @Schema(description = "Total price for this item", example = "59.98")
    private BigDecimal totalPrice;
}
