package com.takeout.service;

import com.takeout.dao.UserDAO;
import com.takeout.model.User;
import java.util.List;

public class UserService {

    private final UserDAO userDAO = new UserDAO();

    public User login(String username, String password) {
        User user = userDAO.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
    public void register(User user) {
        userDAO.addUser(user);
    }
    public void updateUser(User user) {
        userDAO.updateUser(user);
    }
    public void deleteUser(int id) {
        userDAO.deleteUser(id);
    }
    public User getUserById(int id) {
        return userDAO.getUserById(id);
    }
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
}