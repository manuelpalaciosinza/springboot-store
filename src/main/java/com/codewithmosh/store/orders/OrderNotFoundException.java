package com.codewithmosh.store.orders;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message) {
        super(message);
    }
    public OrderNotFoundException(){
      super(("Order not found"));
    }
}
