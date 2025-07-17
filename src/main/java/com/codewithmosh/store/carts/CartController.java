package com.codewithmosh.store.carts;

import com.codewithmosh.store.common.ErrorDto;
import com.codewithmosh.store.products.ProductNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/carts")
@Tag(name = "Carts")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(
            summary = "Create a new shopping cart",
            description = "Creates an empty cart and returns its information",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cart created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartDto.class)))
            }
    )
    @PostMapping
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriComponentsBuilder
    ) {
        var cartDto = cartService.createCart();
        URI uri = uriComponentsBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();
        return ResponseEntity.created(uri).body(cartDto);
    }

    @Operation(
            summary = "Add a product to a cart",
            description = "Adds a product to the specified cart",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Product added successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartItemDto.class))),
                    @ApiResponse(responseCode = "404", description = "Cart not found"),
                    @ApiResponse(responseCode = "400", description = "Product not found or validation error")
            }
    )
    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addToCart(
            @Parameter(description = "The ID of the cart", required = true)
            @PathVariable("cartId") UUID cartId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Product to add to the cart",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AddItemToCartRequest.class))
            )
            @RequestBody AddItemToCartRequest request) {
        var cartItemDto = cartService.addToCar(cartId, request.getProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @Operation(
            summary = "Get cart by ID",
            description = "Retrieves the cart and its items by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cart retrieved successfully",
                            content = @Content(schema = @Schema(implementation = CartDto.class))),
                    @ApiResponse(responseCode = "404", description = "Cart not found")
            }
    )
    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(
            @Parameter(description = "The ID of the cart", required = true)
            @PathVariable("cartId") UUID cartId) {
        var cartDto = cartService.getCart(cartId);
        return ResponseEntity.ok().body(cartDto);
    }

    @Operation(
            summary = "Update quantity of a product in the cart",
            description = "Updates the quantity of a specific product in the cart",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Item updated successfully",
                            content = @Content(schema = @Schema(implementation = CartItemDto.class))),
                    @ApiResponse(responseCode = "404", description = "Cart or product not found"),
                    @ApiResponse(responseCode = "400", description = "Validation error")
            }
    )
    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<CartItemDto> updateItem(
            @Parameter(description = "The ID of the cart", required = true)
            @PathVariable("cartId") UUID cartId,
            @Parameter(description = "The ID of the product", required = true)
            @PathVariable("productId") Long productId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "New quantity for the product",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateCartItemRequest.class))
            )
            @Valid @RequestBody UpdateCartItemRequest request) {
        var cartItemDto = cartService.updateCartItem(cartId, productId, request.getQuantity());
        return ResponseEntity.ok().body(cartItemDto);
    }

    @Operation(
            summary = "Delete a product from the cart",
            description = "Removes a specific product from the cart",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Product removed successfully"),
                    @ApiResponse(responseCode = "404", description = "Cart not found")
            }
    )
    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<Void> deleteItem(
            @Parameter(description = "The ID of the cart", required = true)
            @PathVariable("cartId") UUID cartId,
            @Parameter(description = "The ID of the product", required = true)
            @PathVariable("productId") Long productId) {
        cartService.deleteItem(cartId, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @Operation(
            summary = "Clear all items from the cart",
            description = "Removes all items from the specified cart",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Cart cleared successfully"),
                    @ApiResponse(responseCode = "404", description = "Cart not found")
            }
    )
    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> clearCart(
            @Parameter(description = "The ID of the cart", required = true)
            @PathVariable("cartId") UUID cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorDto> handleCartNotFound(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDto> handleProductNotFound(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(ex.getMessage()));
    }

}
