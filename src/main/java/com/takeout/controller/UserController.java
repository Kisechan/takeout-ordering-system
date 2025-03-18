package com.takeout.controller;

import com.takeout.model.User;
import com.takeout.service.UserService;

public class UserController {

    private final UserService userService = new UserService();

    public User login(String username, String password) {
        return userService.login(username, password);
    }

    public void register(User user) {
        userService.register(user);
    }

    public void updateUser(User user) {
        userService.updateUser(user);
    }

    public void deleteUser(int id) {
        userService.deleteUser(id);
    }

    public User getUserById(int id) {
        return userService.getUserById(id);
    }
}