package com.codewithmosh.store.carts;

import com.codewithmosh.store.products.ProductNotFoundException;
import com.codewithmosh.store.products.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    @Autowired
    public CartService(CartRepository cartRepository, CartMapper cartMapper, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.productRepository = productRepository;
    }

    public CartDto createCart(){
        var cart = new Cart();
        cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    public CartItemDto addToCar(UUID cartId, Long producId){
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null){
            throw new CartNotFoundException("Cart not found");
        }
        var product = productRepository.findById(producId).orElse(null);
        if (product == null){
            throw new ProductNotFoundException("Product not found");
        }
        var cartItem = cart.addItem(product);
        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public CartDto getCart(UUID cartId){
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart==null) throw new CartNotFoundException("Cart not found");
        return cartMapper.toDto(cart);
    }

    public CartItemDto updateCartItem(UUID cartId, Long productId,Integer quantity){
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) throw new CartNotFoundException("Cart not found");
        var cartItem = cart.getItem(productId);
        if (cartItem == null) throw new ProductNotFoundException("Product not found");
        cartItem.setQuantity(quantity);
        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public void deleteItem(UUID cartId,Long productId){
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if(cart == null) throw new CartNotFoundException("Cart not found");
        cart.deleteItem(productId);
        cartRepository.save(cart);
    }

    public void clearCart(UUID cartId){
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart==null) throw new CartNotFoundException("Cart not found");
        cart.clearCart();
        cartRepository.save(cart);
    }
}
