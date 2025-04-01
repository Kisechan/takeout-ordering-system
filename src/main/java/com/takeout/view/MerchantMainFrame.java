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
        setSize(900, 650);
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
        addDishButton.addActionListener(e -> showAddDishDialog());

        // 菜品列表
        JPanel dishPanel = new JPanel();
        dishPanel.setLayout(new BoxLayout(dishPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(dishPanel);

        // 获取当前商家的菜品
        List<Dish> dishes = dishController.getDishesByMerchant(user.getId());

        if (dishes.isEmpty()) {
            dishPanel.add(new JLabel("暂无菜品", SwingConstants.CENTER));
        } else {
            for (Dish dish : dishes) {
                JPanel itemPanel = new JPanel(new BorderLayout());
                itemPanel.setBorder(BorderFactory.createEtchedBorder());
                itemPanel.setBackground(dish.isAvailable() ? Color.WHITE : new Color(240, 240, 240));

                // 菜品信息
                JPanel infoPanel = new JPanel(new GridLayout(3, 1));
                infoPanel.add(new JLabel("名称: " + dish.getName()));
                infoPanel.add(new JLabel("价格: ¥" + dish.getPrice()));
                infoPanel.add(new JLabel("状态: " + (dish.isAvailable() ? "上架" : "已下架")));

                // 菜品描述
                JTextArea descArea = new JTextArea(dish.getDescription());
                descArea.setEditable(false);
                descArea.setLineWrap(true);
                descArea.setBackground(itemPanel.getBackground());

                // 操作按钮
                JPanel buttonPanel = new JPanel();

                JButton toggleButton = new JButton(dish.isAvailable() ? "下架" : "上架");
                toggleButton.addActionListener(e -> {
                    dishController.toggleDishAvailability(dish.getId());
                    showDishesPanel(); // 刷新列表
                });

                JButton editButton = new JButton("编辑");
                editButton.addActionListener(e -> showEditDishDialog(dish));

                JButton deleteButton = new JButton("删除");
                deleteButton.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "确定要删除此菜品吗?", "确认", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        dishController.deleteDish(dish.getId());
                        showDishesPanel(); // 刷新列表
                    }
                });

                buttonPanel.add(toggleButton);
                buttonPanel.add(editButton);
                buttonPanel.add(deleteButton);

                itemPanel.add(infoPanel, BorderLayout.NORTH);
                itemPanel.add(new JScrollPane(descArea), BorderLayout.CENTER);
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

    private void showAddDishDialog() {
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextArea descArea = new JTextArea(5, 20);
        descArea.setLineWrap(true);
        JCheckBox availableCheckBox = new JCheckBox("立即上架", true);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("菜品名称:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("价格:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(priceField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("描述:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(new JScrollPane(descArea), gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(availableCheckBox, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel, "添加菜品",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Dish dish = new Dish();
                dish.setName(nameField.getText());
                dish.setPrice(Double.parseDouble(priceField.getText()));
                dish.setDescription(descArea.getText());
                dish.setAvailable(availableCheckBox.isSelected());
                dish.setMerchant(user);
                dishController.addDish(dish);
                JOptionPane.showMessageDialog(this, "菜品添加成功");
                showDishesPanel(); // 刷新列表
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "价格格式错误");
            }
        }
    }

    private void showEditDishDialog(Dish dish) {
        JTextField nameField = new JTextField(dish.getName());
        JTextField priceField = new JTextField(String.valueOf(dish.getPrice()));
        JTextArea descArea = new JTextArea(dish.getDescription(), 5, 20);
        descArea.setLineWrap(true);
        JCheckBox availableCheckBox = new JCheckBox("上架", dish.isAvailable());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("菜品名称:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("价格:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(priceField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("描述:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(new JScrollPane(descArea), gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(availableCheckBox, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel, "编辑菜品",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                dish.setName(nameField.getText());
                dish.setPrice(Double.parseDouble(priceField.getText()));
                dish.setDescription(descArea.getText());
                dish.setAvailable(availableCheckBox.isSelected());
                dishController.updateDish(dish);
                JOptionPane.showMessageDialog(this, "菜品更新成功");
                showDishesPanel(); // 刷新列表
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "价格格式错误");
            }
        }
    }

    private void showOrdersPanel() {
        mainPanel.removeAll();
        JPanel orderPanel = new JPanel();
        orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(orderPanel);

        // 获取当前商家的订单
        List<Order> orders = orderController.getOrdersByMerchant(user.getId());

        if (orders.isEmpty()) {
            orderPanel.add(new JLabel("暂无订单", SwingConstants.CENTER));
        } else {
            for (Order order : orders) {
                JPanel itemPanel = new JPanel(new BorderLayout());
                itemPanel.setBorder(BorderFactory.createEtchedBorder());

                Dish dish = order.getDish();
                JLabel infoLabel = new JLabel(
                        "<html>订单ID: " + order.getId() +
                                "<br>顾客: " + order.getCustomer().getUsername() +
                                "<br>菜品: " + dish.getName() + " × " + order.getQuantity() +
                                "<br>总价: ¥" + order.getTotalPrice() +
                                "<br>状态: " + order.getStatus() +
                                "<br>下单时间: " + order.getAddTime() + "</html>");

                JPanel buttonPanel = new JPanel();

                if (order.getStatus() == OrderStatus.pending) {
                    JButton completeButton = new JButton("完成订单");
                    completeButton.addActionListener(e -> {
                        order.setStatus(OrderStatus.completed);
                        orderController.updateOrder(order);
                        showOrdersPanel(); // 刷新订单列表
                    });
                    buttonPanel.add(completeButton);
                }

                itemPanel.add(infoLabel, BorderLayout.CENTER);
                itemPanel.add(buttonPanel, BorderLayout.SOUTH);
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
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.EAST;

        // 获取最新用户信息
        User currentUser = userController.getUserById(user.getId());

        // 用户名
        gbc.gridx = 0; gbc.gridy = 0;
        profilePanel.add(new JLabel("用户名:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        JTextField usernameField = new JTextField(currentUser.getUsername(), 20);
        usernameField.setEditable(false);
        profilePanel.add(usernameField, gbc);

        // 电话
        gbc.gridx = 0; gbc.gridy = 1;
        profilePanel.add(new JLabel("电话:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        JTextField phoneField = new JTextField(currentUser.getPhone(), 20);
        profilePanel.add(phoneField, gbc);

        // 地址
        gbc.gridx = 0; gbc.gridy = 2;
        profilePanel.add(new JLabel("地址:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        JTextField addressField = new JTextField(currentUser.getAddress(), 20);
        profilePanel.add(addressField, gbc);

        // 保存按钮
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JButton saveButton = new JButton("保存修改");
        saveButton.addActionListener(e -> {
            currentUser.setPhone(phoneField.getText());
            currentUser.setAddress(addressField.getText());
            userController.updateUser(currentUser);
            JOptionPane.showMessageDialog(this, "信息更新成功");
        });
        profilePanel.add(saveButton, gbc);

        // 修改密码按钮
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        JButton changePassButton = new JButton("修改密码");
        changePassButton.addActionListener(e -> showChangePasswordDialog(currentUser));
        profilePanel.add(changePassButton, gbc);

        mainPanel.add(profilePanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showChangePasswordDialog(User currentUser) {
        JPasswordField oldPassField = new JPasswordField();
        JPasswordField newPassField = new JPasswordField();
        JPasswordField confirmPassField = new JPasswordField();

        JPanel passPanel = new JPanel(new GridLayout(3, 2, 5, 5));
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
            } else if (newPass.length() < 6) {
                JOptionPane.showMessageDialog(this, "密码长度不能少于6位");
            } else {
                currentUser.setPassword(newPass);
                userController.updateUser(currentUser);
                JOptionPane.showMessageDialog(this, "密码修改成功");
            }
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "确定要退出登录吗?", "确认", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}