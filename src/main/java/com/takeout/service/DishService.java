package com.takeout.service;

import com.takeout.dao.DishDAO;
import com.takeout.model.Dish;
import java.util.List;

public class DishService {
    private final DishDAO dishDAO = new DishDAO();

    public Dish getDishById(int id) {
        return dishDAO.getDishById(id);
    }

    public List<Dish> getDishesByMerchant(int merchantId) {
        return dishDAO.getDishesByMerchant(merchantId);
    }

    public List<Dish> getAvailableDishesByMerchant(int merchantId) {
        return dishDAO.getAvailableDishesByMerchant(merchantId);
    }

    public void addDish(Dish dish) {
        dishDAO.addDish(dish);
    }

    public void updateDish(Dish dish) {
        dishDAO.updateDish(dish);
    }

    public void deleteDish(int id) {
        dishDAO.deleteDish(id);
    }

    public List<Dish> getAllDishes() {
        return dishDAO.getAllDishes();
    }

    public void toggleDishAvailability(int dishId) {
        dishDAO.toggleDishAvailability(dishId);
    }
}