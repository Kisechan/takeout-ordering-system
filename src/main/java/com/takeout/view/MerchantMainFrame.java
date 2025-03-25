package com.takeout.view;

import com.takeout.controller.*;
import com.takeout.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MerchantMainFrame extends JFrame {
    private User user;
    private JPanel mainPanel;
    private DishController dishController = new DishController();
    private OrderController orderController = new OrderController();
    private UserController userController = new UserController();

    public MerchantMainFrame(User user) {
        this.user = user;
        setTitle("商家主页 - " + user.getUsername());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 创建导航栏
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new GridLayout(1, 4));

        JButton dishesButton = new JButton("菜品管理");
        JButton ordersButton = new JButton("订单管理");
        JButton profileButton = new JButton("个人信息");
        JButton logoutButton = new JButton("退出登录");

        navPanel.add(dishesButton);
        navPanel.add(ordersButton);
        navPanel.add(profileButton);
        navPanel.add(logoutButton);

        // 主内容面板
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // 默认显示欢迎信息
        showWelcomePanel();

        // 添加导航栏和主内容面板
        add(navPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        // 导航栏按钮事件
        dishesButton.addActionListener(e -> showDishesPanel());
        ordersButton.addActionListener(e -> showOrdersPanel());
        profileButton.addActionListener(e -> showProfilePanel());
        logoutButton.addActionListener(e -> logout());
    }

    private void showWelcomePanel() {
        mainPanel.removeAll();
        JLabel welcomeLabel = new JLabel("欢迎, " + user.getUsername() + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        mainPanel.add(welcomeLabel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showDishesPanel() {
        mainPanel.removeAll();

        // 添加菜品按钮
        JButton addDishButton = new JButton("添加菜品");
        addDishButton.addActionListener(e -> {
            JTextField nameField = new JTextField();
            JTextField priceField = new JTextField();
            JTextArea descArea = new JTextArea(5, 20);
            descArea.setLineWrap(true);

            JPanel panel = new JPanel(new GridLayout(4, 2));
            panel.add(new JLabel("菜品名称:"));
            panel.add(nameField);
            panel.add(new JLabel("价格:"));
            panel.add(priceField);
            panel.add(new JLabel("描述:"));
            panel.add(new JScrollPane(descArea));

            int result = JOptionPane.showConfirmDialog(this, panel, "添加菜品",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    Dish dish = new Dish();
                    dish.setName(nameField.getText());
                    dish.setPrice(Double.parseDouble(priceField.getText()));
                    dish.setDescription(descArea.getText());
                    dish.setMerchant(user);
                    dishController.addDish(dish);
                    JOptionPane.showMessageDialog(this, "菜品添加成功");
                    showDishesPanel(); // 刷新列表
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "价格格式错误");
                }
            }
        });

        // 菜品列表
        JPanel dishPanel = new JPanel();
        dishPanel.setLayout(new BoxLayout(dishPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(dishPanel);

        // 获取当前商家的菜品
        List<Dish> dishes = dishController.getDishesByMerchant(user.getId());

        if (dishes.isEmpty()) {
            dishPanel.add(new JLabel("暂无菜品"));
        } else {
            for (Dish dish : dishes) {
                JPanel itemPanel = new JPanel(new BorderLayout());
                itemPanel.setBorder(BorderFactory.createEtchedBorder());

                JLabel nameLabel = new JLabel(dish.getName() + " - ¥" + dish.getPrice());
                JTextArea descArea = new JTextArea(dish.getDescription());
                descArea.setEditable(false);
                descArea.setLineWrap(true);

                JButton editButton = new JButton("编辑");
                editButton.addActionListener(e -> {
                    JTextField editNameField = new JTextField(dish.getName());
                    JTextField editPriceField = new JTextField(String.valueOf(dish.getPrice()));
                    JTextArea editDescArea = new JTextArea(dish.getDescription(), 5, 20);
                    editDescArea.setLineWrap(true);

                    JPanel editPanel = new JPanel(new GridLayout(4, 2));
                    editPanel.add(new JLabel("菜品名称:"));
                    editPanel.add(editNameField);
                    editPanel.add(new JLabel("价格:"));
                    editPanel.add(editPriceField);
                    editPanel.add(new JLabel("描述:"));
                    editPanel.add(new JScrollPane(editDescArea));

                    int result = JOptionPane.showConfirmDialog(this, editPanel, "编辑菜品",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {
                        try {
                            dish.setName(editNameField.getText());
                            dish.setPrice(Double.parseDouble(editPriceField.getText()));
                            dish.setDescription(editDescArea.getText());
                            dishController.updateDish(dish);
                            JOptionPane.showMessageDialog(this, "菜品更新成功");
                            showDishesPanel(); // 刷新列表
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "价格格式错误");
                        }
                    }
                });

                JButton deleteButton = new JButton("删除");
                deleteButton.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "确定要删除此菜品吗?", "确认", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        dishController.deleteDish(dish.getId());
                        showDishesPanel(); // 刷新列表
                    }
                });

                JPanel buttonPanel = new JPanel();
                buttonPanel.add(editButton);
                buttonPanel.add(deleteButton);

                itemPanel.add(nameLabel, BorderLayout.NORTH);
                itemPanel.add(descArea, BorderLayout.CENTER);
                itemPanel.add(buttonPanel, BorderLayout.SOUTH);

                dishPanel.add(itemPanel);
            }
        }

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(addDishButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showOrdersPanel() {
        mainPanel.removeAll();
        JPanel orderPanel = new JPanel();
        orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(orderPanel);

        // 获取当前商家的订单
        List<Order> orders = orderController.getOrdersByMerchant(user.getId());

        if (orders.isEmpty()) {
            orderPanel.add(new JLabel("暂无订单"));
        } else {
            for (Order order : orders) {
                JPanel itemPanel = new JPanel(new BorderLayout());
                itemPanel.setBorder(BorderFactory.createEtchedBorder());

                Dish dish = order.getDish();
                JLabel infoLabel = new JLabel("顾客: " + order.getCustomer().getUsername() +
                        " - " + dish.getName() + " × " + order.getQuantity() +
                        " - ¥" + order.getTotalPrice() + " - 状态: " + order.getStatus());

                if (order.getStatus() == OrderStatus.pending) {
                    JButton completeButton = new JButton("完成订单");
                    completeButton.addActionListener(e -> {
                        order.setStatus(OrderStatus.completed);
                        orderController.updateOrder(order);
                        showOrdersPanel(); // 刷新订单列表
                    });

                    JPanel buttonPanel = new JPanel();
                    buttonPanel.add(completeButton);
                    itemPanel.add(buttonPanel, BorderLayout.EAST);
                }

                itemPanel.add(infoLabel, BorderLayout.CENTER);
                orderPanel.add(itemPanel);
            }
        }

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showProfilePanel() {
        mainPanel.removeAll();
        JPanel profilePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.EAST;

        // 获取最新用户信息
        User currentUser = userController.getUserById(user.getId());

        JLabel usernameLabel = new JLabel("用户名:");
        JTextField usernameField = new JTextField(currentUser.getUsername(), 20);
        usernameField.setEditable(false);

        JLabel phoneLabel = new JLabel("电话:");
        JTextField phoneField = new JTextField(currentUser.getPhone(), 20);

        JLabel addressLabel = new JLabel("地址:");
        JTextField addressField = new JTextField(currentUser.getAddress(), 20);

        JButton saveButton = new JButton("保存修改");
        saveButton.addActionListener(e -> {
            currentUser.setPhone(phoneField.getText());
            currentUser.setAddress(addressField.getText());
            userController.updateUser(currentUser);
            JOptionPane.showMessageDialog(this, "信息更新成功");
        });

        JButton changePassButton = new JButton("修改密码");
        changePassButton.addActionListener(e -> {
            JPasswordField oldPassField = new JPasswordField();
            JPasswordField newPassField = new JPasswordField();
            JPasswordField confirmPassField = new JPasswordField();

            JPanel passPanel = new JPanel(new GridLayout(3, 2));
            passPanel.add(new JLabel("旧密码:"));
            passPanel.add(oldPassField);
            passPanel.add(new JLabel("新密码:"));
            passPanel.add(newPassField);
            passPanel.add(new JLabel("确认密码:"));
            passPanel.add(confirmPassField);

            int result = JOptionPane.showConfirmDialog(this, passPanel, "修改密码",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String oldPass = new String(oldPassField.getPassword());
                String newPass = new String(newPassField.getPassword());
                String confirmPass = new String(confirmPassField.getPassword());

                if (!oldPass.equals(currentUser.getPassword())) {
                    JOptionPane.showMessageDialog(this, "旧密码错误");
                } else if (!newPass.equals(confirmPass)) {
                    JOptionPane.showMessageDialog(this, "两次输入的新密码不一致");
                } else {
                    currentUser.setPassword(newPass);
                    userController.updateUser(currentUser);
                    JOptionPane.showMessageDialog(this, "密码修改成功");
                }
            }
        });

        // 添加组件到面板
        gbc.gridx = 0; gbc.gridy = 0;
        profilePanel.add(usernameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        profilePanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        profilePanel.add(phoneLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        profilePanel.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        profilePanel.add(addressLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        profilePanel.add(addressField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        profilePanel.add(saveButton, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        profilePanel.add(changePassButton, gbc);

        mainPanel.add(profilePanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "确定要退出登录吗?", "确认",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}