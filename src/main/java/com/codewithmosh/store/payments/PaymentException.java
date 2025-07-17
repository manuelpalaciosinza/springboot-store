package com.codewithmosh.store.payments;

public class PaymentException extends RuntimeException {
    public PaymentException(String message) {
        super(message);
    }
    public PaymentException() {
        super("There was an error creating the checkout session");
    }
}
