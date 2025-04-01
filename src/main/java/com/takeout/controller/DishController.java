package com.takeout.controller;

import com.takeout.model.Dish;
import com.takeout.service.DishService;
import java.util.List;

public class DishController {
    private final DishService dishService = new DishService();

    public Dish getDishById(int id) {
        return dishService.getDishById(id);
    }

    public List<Dish> getDishesByMerchant(int merchantId) {
        return dishService.getDishesByMerchant(merchantId);
    }

    public List<Dish> getAvailableDishesByMerchant(int merchantId) {
        return dishService.getAvailableDishesByMerchant(merchantId);
    }

    public void addDish(Dish dish) {
        dishService.addDish(dish);
    }

    public void updateDish(Dish dish) {
        dishService.updateDish(dish);
    }

    public void deleteDish(int id) {
        dishService.deleteDish(id);
    }

    public List<Dish> getAllDishes() {
        return dishService.getAllDishes();
    }

    public void toggleDishAvailability(int dishId) {
        dishService.toggleDishAvailability(dishId);
    }
}