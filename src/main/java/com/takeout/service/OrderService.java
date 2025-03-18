package com.takeout.service;

import com.takeout.dao.OrderDAO;
import com.takeout.model.Order;
import java.util.List;

public class OrderService {

    private final OrderDAO orderDAO = new OrderDAO();

    public Order getOrderById(int id) {
        return orderDAO.getOrderById(id);
    }
    public List<Order> getOrdersByCustomer(int customerId) {
        return orderDAO.getOrdersByCustomer(customerId);
    }
    public List<Order> getOrdersByMerchant(int merchantId) {
        return orderDAO.getOrdersByMerchant(merchantId);
    }
    public void addOrder(Order order) {
        orderDAO.addOrder(order);
    }
    public void updateOrder(Order order) {
        orderDAO.updateOrder(order);
    }
    public void deleteOrder(int id) {
        orderDAO.deleteOrder(id);
    }
}