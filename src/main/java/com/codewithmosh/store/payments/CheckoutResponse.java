package com.codewithmosh.store.payments;

import lombok.Data;

@Data
public class CheckoutResponse {
    long orderId;
    private String checkoutUrl;

    public CheckoutResponse(long orderId,String checkoutUrl) {
        this.orderId = orderId;
        this.checkoutUrl = checkoutUrl;

    }
}
