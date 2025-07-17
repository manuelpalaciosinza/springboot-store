package com.codewithmosh.store.carts;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Request to add a product to the cart")
public class AddItemToCartRequest {
    @Schema(description = "ID of the product to add", example = "1")
    @NotNull
    private Long productId;
}
