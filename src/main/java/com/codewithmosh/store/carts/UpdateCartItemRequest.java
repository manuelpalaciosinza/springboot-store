package com.codewithmosh.store.carts;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Request to update the quantity of a product in the cart")
public class UpdateCartItemRequest {

    @NotNull(message = "Quantity must be provided")
    @Min(value = 1, message = "Quantity must be greater than 0")
    @Schema(description = "New quantity for the product", example = "3")
    private Integer quantity;
    // We use Integer instead of int because the default of int is 0, and if we enter an empty JSON
    // the value will be set to 0 instead of null
}
