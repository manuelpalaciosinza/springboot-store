package com.codewithmosh.store.carts;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Schema(description = "Represents a shopping cart with items and total price")
public class CartDto {

    @Schema(description = "ID of the cart", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    private UUID id;

    @Schema(description = "List of items in the cart")
    private List<CartItemDto> items = new ArrayList<>();

    @Schema(description = "Total price of the cart", example = "199.99")
    private BigDecimal totalPrice = BigDecimal.ZERO;
}
