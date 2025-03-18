package com.takeout.dao;

import com.takeout.model.Dish;
import com.takeout.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class DishDAO {
    public Dish getDishById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Dish.class, id);
        }
    }
    public List<Dish> getDishesByMerchant(int merchantId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Dish> query = session.createQuery("FROM Dish WHERE merchant.id = :merchantId", Dish.class);
            query.setParameter("merchantId", merchantId);
            return query.list();
        }
    }
    public void addDish(Dish dish) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(dish);
            transaction.commit();
        }
    }
    public void updateDish(Dish dish) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(dish);
            transaction.commit();
        }
    }
    public void deleteDish(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Dish dish = session.get(Dish.class, id);
            if (dish != null) {
                session.delete(dish);
            }
            transaction.commit();
        }
    }
    public List<Dish> getAllDishes() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Dish", Dish.class).list();
        }
    }
}