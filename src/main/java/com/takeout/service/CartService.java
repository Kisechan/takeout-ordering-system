package com.takeout.service;

import com.takeout.dao.CartDAO;
import com.takeout.model.Cart;
import java.util.List;

public class CartService {

    private final CartDAO cartDAO = new CartDAO();

    public Cart getCartById(int id) {
        return cartDAO.getCartById(id);
    }
    public List<Cart> getCartsByCustomer(int customerId) {
        return cartDAO.getCartsByCustomer(customerId);
    }
    public void addCart(Cart cart) {
        cartDAO.addCart(cart);
    }
    public void updateCart(Cart cart) {
        cartDAO.updateCart(cart);
    }
    public void deleteCart(int id) {
        cartDAO.deleteCart(id);
    }
}