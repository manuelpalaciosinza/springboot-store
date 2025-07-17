package com.codewithmosh.store.orders;

import com.codewithmosh.store.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderService(AuthService authService, OrderRepository orderRepository, OrderMapper orderMapper) {
        this.authService = authService;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public List<OrderDto> getAllOrders(){
        var user = authService.getCurrentUser();
        var orders = orderRepository.getOrdersByCustomer(user);
        return orders.stream().map(orderMapper::toDto)
                .toList();
    }

    public OrderDto getOrder(Long orderId){
        var order = orderRepository.getOrderWithItems(orderId);
        if (order.isEmpty()){
            throw new OrderNotFoundException();
        }
        var user = authService.getCurrentUser();
        if(!order.get().isPlacedBy(user)){
            throw new AccessDeniedException("You dont have access to this order");
        }
        return orderMapper.toDto(order.get());
    }
}
