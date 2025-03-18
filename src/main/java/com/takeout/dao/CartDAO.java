package com.takeout.dao;

import com.takeout.model.Cart;
import com.takeout.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class CartDAO {

    public Cart getCartById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Cart.class, id);
        }
    }
    public List<Cart> getCartsByCustomer(int customerId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Cart> query = session.createQuery("FROM Cart WHERE customer.id = :customerId", Cart.class);
            query.setParameter("customerId", customerId);
            return query.list();
        }
    }
    public void addCart(Cart cart) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(cart);
            transaction.commit();
        }
    }
    public void updateCart(Cart cart) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(cart);
            transaction.commit();
        }
    }
    public void deleteCart(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Cart cart = session.get(Cart.class, id);
            if (cart != null) {
                session.delete(cart);
            }
            transaction.commit();
        }
    }
}