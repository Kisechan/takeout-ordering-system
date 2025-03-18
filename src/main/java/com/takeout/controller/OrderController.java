package com.takeout.controller;

import com.takeout.model.Order;
import com.takeout.service.OrderService;

import java.util.List;

public class OrderController {

    private final OrderService orderService = new OrderService();

    public Order getOrderById(int id) {
        return orderService.getOrderById(id);
    }

    public List<Order> getOrdersByCustomer(int customerId) {
        return orderService.getOrdersByCustomer(customerId);
    }

    public List<Order> getOrdersByMerchant(int merchantId) {
        return orderService.getOrdersByMerchant(merchantId);
    }

    public void addOrder(Order order) {
        orderService.addOrder(order);
    }

    public void updateOrder(Order order) {
        orderService.updateOrder(order);
    }

    public void deleteOrder(int id) {
        orderService.deleteOrder(id);
    }
}