package com.codewithmosh.store.carts;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String message) {
        super(message);
    }
    public CartNotFoundException(){
        super("Cart not found");
    }
}
