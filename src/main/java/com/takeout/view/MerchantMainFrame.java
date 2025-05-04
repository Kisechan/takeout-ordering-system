package com.takeout.view;

import com.takeout.controller.*;
import com.takeout.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.time.LocalDateTime;

public class MerchantMainFrame extends JFrame {
    private static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private static final Color SECONDARY_COLOR = new Color(220, 220, 220);
    private static final Font TITLE_FONT = new Font("微软雅黑", Font.BOLD, 16);
    private static final Font NORMAL_FONT = new Font("微软雅黑", Font.PLAIN, 14);
    private static final int CARD_WIDTH = 280;

    private User user;
    private JPanel mainPanel;
    private DishController dishController = new DishController();
    private OrderController orderController = new OrderController();
    private UserController userController = new UserController();

    public MerchantMainFrame(User user) {
        this.user = user;
        setTitle("商家主页 - " + user.getUsername());
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 创建导航栏
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new GridLayout(1, 4, 5, 5));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        navPanel.setBackground(PRIMARY_COLOR);

        JButton dishesButton = createStyledButton("菜品管理");
        JButton ordersButton = createStyledButton("订单管理");
        JButton profileButton = createStyledButton("个人信息");
        JButton logoutButton = createStyledButton("退出登录");

        navPanel.add(dishesButton);
        navPanel.add(ordersButton);
        navPanel.add(profileButton);
        navPanel.add(logoutButton);

        // 主内容面板
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(NORMAL_FONT);
        button.setBackground(Color.WHITE);
        button.setForeground(PRIMARY_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(240, 240, 240));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
            }
        });
        return button;
    }

    private JPanel createCardPanel() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SECONDARY_COLOR, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(CARD_WIDTH, 200));
        return card;
    }

    private void showWelcomePanel() {
        mainPanel.removeAll();
        JLabel welcomeLabel = new JLabel("欢迎, " + user.getUsername() + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        welcomeLabel.setForeground(PRIMARY_COLOR);
        mainPanel.add(welcomeLabel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showDishesPanel() {
        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout());

        // 顶部操作面板
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JButton addDishButton = createStyledButton("添加菜品");
        addDishButton.addActionListener(e -> showAddDishDialog());
        topPanel.add(addDishButton);

        // 菜品卡片面板
        JPanel cardsPanel = new JPanel(new WrapLayout(FlowLayout.LEFT, 15, 15));
        cardsPanel.setBackground(new Color(245, 245, 245));
        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // 获取当前商家的菜品
        List<Dish> dishes = dishController.getDishesByMerchant(user.getId());

        if (dishes.isEmpty()) {
            JLabel emptyLabel = new JLabel("暂无菜品", SwingConstants.CENTER);
            emptyLabel.setFont(TITLE_FONT);
            emptyLabel.setForeground(PRIMARY_COLOR);
            cardsPanel.add(emptyLabel);
        } else {
            for (Dish dish : dishes) {
                JPanel card = createCardPanel();
                card.setBackground(dish.isAvailable() ? Color.WHITE : new Color(240, 240, 240));

                // 菜品信息区域
                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                infoPanel.setBackground(card.getBackground());

                JLabel nameLabel = new JLabel(dish.getName());
                nameLabel.setFont(TITLE_FONT);
                nameLabel.setForeground(PRIMARY_COLOR);

                JLabel priceLabel = new JLabel("¥" + dish.getPrice());
                priceLabel.setFont(NORMAL_FONT);

                JLabel statusLabel = new JLabel("状态: " + (dish.isAvailable() ? "上架" : "已下架"));
                statusLabel.setFont(NORMAL_FONT);

                JTextArea descArea = new JTextArea(dish.getDescription());
                descArea.setEditable(false);
                descArea.setLineWrap(true);
                descArea.setWrapStyleWord(true);
                descArea.setBackground(card.getBackground());
                descArea.setFont(NORMAL_FONT);

                infoPanel.add(nameLabel);
                infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                infoPanel.add(priceLabel);
                infoPanel.add(statusLabel);
                infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                infoPanel.add(new JScrollPane(descArea));

                // 操作按钮区域
                JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 5, 0));
                buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

                JButton toggleButton = createStyledButton(dish.isAvailable() ? "下架" : "上架");
                toggleButton.addActionListener(e -> {
                    dishController.toggleDishAvailability(dish.getId());
                    showDishesPanel();
                });

                JButton editButton = createStyledButton("编辑");
                editButton.addActionListener(e -> showEditDishDialog(dish));

                JButton deleteButton = createStyledButton("删除");
                deleteButton.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "确定要删除此菜品吗?", "确认", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        dishController.deleteDish(dish.getId());
                        showDishesPanel();
                    }
                });

                buttonPanel.add(toggleButton);
                buttonPanel.add(editButton);
                buttonPanel.add(deleteButton);

                card.add(infoPanel, BorderLayout.CENTER);
                card.add(buttonPanel, BorderLayout.SOUTH);

                cardsPanel.add(card);
            }
        }

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showOrdersPanel() {
        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout());

        // 订单卡片面板
        JPanel cardsPanel = new JPanel(new WrapLayout(FlowLayout.LEFT, 15, 15));
        cardsPanel.setBackground(new Color(245, 245, 245));
        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // 获取当前商家的订单
        List<Order> orders = orderController.getOrdersByMerchant(user.getId());

        if (orders.isEmpty()) {
            JLabel emptyLabel = new JLabel("暂无订单", SwingConstants.CENTER);
            emptyLabel.setFont(TITLE_FONT);
            emptyLabel.setForeground(PRIMARY_COLOR);
            cardsPanel.add(emptyLabel);
        } else {
            for (Order order : orders) {
                JPanel card = createCardPanel();
                card.setPreferredSize(new Dimension(CARD_WIDTH, 220));

                Dish dish = order.getDish();

                // 订单信息区域
                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                infoPanel.setBackground(Color.WHITE);

                JLabel orderIdLabel = new JLabel("订单ID: " + order.getId());
                orderIdLabel.setFont(NORMAL_FONT);

                JLabel customerLabel = new JLabel("顾客: " + order.getCustomer().getUsername());
                customerLabel.setFont(NORMAL_FONT);

                JLabel dishLabel = new JLabel("菜品: " + dish.getName() + " × " + order.getQuantity());
                dishLabel.setFont(NORMAL_FONT);

                JLabel priceLabel = new JLabel("总价: ¥" + order.getTotalPrice());
                priceLabel.setFont(NORMAL_FONT);

                JLabel statusLabel = new JLabel("状态: " + StatusMap.translate(order.getStatus().name()));
                statusLabel.setFont(NORMAL_FONT);
                statusLabel.setForeground(order.getStatus() == OrderStatus.completed ? Color.GREEN : PRIMARY_COLOR);

                JLabel timeLabel = new JLabel("时间: " + order.getAddTime().toLocalDate());
                timeLabel.setFont(NORMAL_FONT);

                infoPanel.add(orderIdLabel);
                infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                infoPanel.add(customerLabel);
                infoPanel.add(dishLabel);
                infoPanel.add(priceLabel);
                infoPanel.add(statusLabel);
                infoPanel.add(timeLabel);

                // 操作按钮区域
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
                buttonPanel.setBackground(Color.WHITE);

                if (order.getStatus() != OrderStatus.completed) {
                    JButton completeButton = createStyledButton("完成订单");
                    completeButton.addActionListener(e -> {
                        order.setStatus(OrderStatus.completed);
                        orderController.updateOrder(order);
                        showOrdersPanel();
                    });
                    buttonPanel.add(completeButton);
                }

                card.add(infoPanel, BorderLayout.CENTER);
                card.add(buttonPanel, BorderLayout.SOUTH);

                cardsPanel.add(card);
            }
        }

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showProfilePanel() {
        mainPanel.removeAll();
        JPanel profilePanel = new JPanel(new GridBagLayout());
        profilePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
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
        profilePanel.add(new JLabel("电话*:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        JTextField phoneField = new JTextField(currentUser.getPhone(), 20);
        // 电话验证: 11位数字
        phoneField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String text = ((JTextField) input).getText();
                return text.matches("\\d{11}");
            }
        });
        profilePanel.add(phoneField, gbc);

        // 地址
        gbc.gridx = 0; gbc.gridy = 2;
        profilePanel.add(new JLabel("地址:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        JTextField addressField = new JTextField(currentUser.getAddress(), 20);
        // 地址验证: 不超过100字符
        addressField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String text = ((JTextField) input).getText();
                return text.length() <= 100;
            }
        });
        profilePanel.add(addressField, gbc);

        // 保存按钮
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JButton saveButton = createStyledButton("保存修改");
        saveButton.addActionListener(e -> {
            // 验证输入
            if (!phoneField.getInputVerifier().verify(phoneField)) {
                JOptionPane.showMessageDialog(this, "请输入有效的11位手机号码", "输入错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!addressField.getInputVerifier().verify(addressField)) {
                JOptionPane.showMessageDialog(this, "地址长度不能超过100个字符", "输入错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            currentUser.setPhone(phoneField.getText().trim());
            currentUser.setAddress(addressField.getText().trim());
            try {
                userController.updateUser(currentUser);
                JOptionPane.showMessageDialog(this, "信息更新成功", "成功", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "更新信息失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        profilePanel.add(saveButton, gbc);

        // 修改密码按钮
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        JButton changePassButton = createStyledButton("修改密码");
        changePassButton.addActionListener(e -> showChangePasswordDialog(currentUser));
        profilePanel.add(changePassButton, gbc);

        // 添加提示标签
        JLabel hintLabel = new JLabel("带*的为必填项");
        hintLabel.setFont(new Font("微软雅黑", Font.ITALIC, 12));
        hintLabel.setForeground(Color.GRAY);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        profilePanel.add(hintLabel, gbc);

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

    private void showAddDishDialog() {
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextArea descArea = new JTextArea(5, 20);
        descArea.setLineWrap(true);
        JCheckBox availableCheckBox = new JCheckBox("立即上架", true);

        // 设置输入验证
        nameField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String text = ((JTextField) input).getText();
                return !text.trim().isEmpty() && text.length() <= 50; // 名称不能为空且不超过50字符
            }
        });

        priceField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                try {
                    double price = Double.parseDouble(((JTextField) input).getText());
                    return price > 0 && price <= 9999; // 价格必须为正数且不超过9999
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        });

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("菜品名称*:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("价格*:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(priceField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("描述:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(new JScrollPane(descArea), gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(availableCheckBox, gbc);

        // 添加提示标签
        JLabel hintLabel = new JLabel("带*的为必填项");
        hintLabel.setFont(new Font("微软雅黑", Font.ITALIC, 12));
        hintLabel.setForeground(Color.GRAY);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(hintLabel, gbc);

        while (true) {
            int result = JOptionPane.showConfirmDialog(this, panel, "添加菜品",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                // 验证输入
                if (!nameField.getInputVerifier().verify(nameField)) {
                    JOptionPane.showMessageDialog(this, "请输入有效的菜品名称(1-50个字符)", "输入错误", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if (!priceField.getInputVerifier().verify(priceField)) {
                    JOptionPane.showMessageDialog(this, "请输入有效的价格(0-9999之间的数字)", "输入错误", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                try {
                    Dish dish = new Dish();
                    dish.setName(nameField.getText().trim());
                    dish.setPrice(Double.parseDouble(priceField.getText()));
                    dish.setDescription(descArea.getText().trim());
                    dish.setAvailable(availableCheckBox.isSelected());
                    dish.setMerchant(user);
                    dishController.addDish(dish);
                    JOptionPane.showMessageDialog(this, "菜品添加成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                    showDishesPanel();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "添加菜品失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
                break;
            } else {
                break;
            }
        }
    }

    private void showEditDishDialog(Dish dish) {
        JTextField nameField = new JTextField(dish.getName());
        JTextField priceField = new JTextField(String.valueOf(dish.getPrice()));
        JTextArea descArea = new JTextArea(dish.getDescription() == null ? "" : dish.getDescription(), 5, 20);
        descArea.setLineWrap(true);
        JCheckBox availableCheckBox = new JCheckBox("上架", dish.isAvailable());

        // 设置输入验证
        nameField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String text = ((JTextField) input).getText();
                return !text.trim().isEmpty() && text.length() <= 50;
            }
        });

        priceField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                try {
                    double price = Double.parseDouble(((JTextField) input).getText());
                    return price > 0 && price <= 9999;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        });

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("菜品名称*:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("价格*:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(priceField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("描述:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(new JScrollPane(descArea), gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(availableCheckBox, gbc);

        // 添加提示标签
        JLabel hintLabel = new JLabel("带*的为必填项");
        hintLabel.setFont(new Font("微软雅黑", Font.ITALIC, 12));
        hintLabel.setForeground(Color.GRAY);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(hintLabel, gbc);

        while (true) {
            int result = JOptionPane.showConfirmDialog(this, panel, "编辑菜品",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                // 验证输入
                if (!nameField.getInputVerifier().verify(nameField)) {
                    JOptionPane.showMessageDialog(this, "请输入有效的菜品名称(1-50个字符)", "输入错误", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if (!priceField.getInputVerifier().verify(priceField)) {
                    JOptionPane.showMessageDialog(this, "请输入有效的价格(0-9999之间的数字)", "输入错误", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                try {
                    dish.setName(nameField.getText().trim());
                    dish.setPrice(Double.parseDouble(priceField.getText()));
                    dish.setDescription(descArea.getText().trim());
                    dish.setAvailable(availableCheckBox.isSelected());
                    dishController.updateDish(dish);
                    JOptionPane.showMessageDialog(this, "菜品更新成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                    showDishesPanel();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "更新菜品失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
                break;
            } else {
                break;
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