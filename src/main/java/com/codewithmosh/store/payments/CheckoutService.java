package com.codewithmosh.store.payments;

import com.codewithmosh.store.orders.Order;
import com.codewithmosh.store.carts.CartEmptyException;
import com.codewithmosh.store.carts.CartNotFoundException;
import com.codewithmosh.store.carts.CartRepository;
import com.codewithmosh.store.orders.OrderRepository;
import com.codewithmosh.store.auth.AuthService;
import com.codewithmosh.store.carts.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final AuthService authService;
    private final PaymentGateway paymentGateway;

    @Autowired
    public CheckoutService(CartRepository cartRepository, OrderRepository orderRepository, CartService cartService, AuthService authService, PaymentGateway paymentGateway) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.authService = authService;
        this.paymentGateway = paymentGateway;
    }

    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request) throws PaymentException {
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        if (cart.isEmpty()) {
            throw new CartEmptyException();
        }

        var order = Order.fromCart(cart, authService.getCurrentUser());
        orderRepository.save(order);

        try {
            var session = paymentGateway.createCheckoutSession(order);
            cartService.clearCart(request.getCartId());
            return new CheckoutResponse(order.getId(), session.getCheckoutUrl());

        } catch (PaymentException ex) {
            orderRepository.delete(order);
            throw ex;
        }
    }

    public void handleWebhookEvent(WebhookRequest request){
        paymentGateway.parseWebhookRequest(request)
                .ifPresent(paymentResult ->{
                    var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
                    order.setStatus(paymentResult.getPaymentStatus());
                    orderRepository.save(order);
                } );
    }
}
