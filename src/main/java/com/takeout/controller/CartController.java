package com.takeout.controller;

import com.takeout.model.Cart;
import com.takeout.service.CartService;

import java.util.List;

public class CartController {

    private final CartService cartService = new CartService();

    public Cart getCartById(int id) {
        return cartService.getCartById(id);
    }

    public List<Cart> getCartsByCustomer(int customerId) {
        return cartService.getCartsByCustomer(customerId);
    }

    public void addCart(Cart cart) {
        cartService.addCart(cart);
    }

    public void updateCart(Cart cart) {
        cartService.updateCart(cart);
    }

    public void deleteCart(int id) {
        cartService.deleteCart(id);
    }
}