package com.codewithmosh.store.carts;

public class CartEmptyException extends RuntimeException {
    public CartEmptyException(String message) {
        super(message);
    }
    public CartEmptyException(){
      super("Cart is empty");
    }
}
