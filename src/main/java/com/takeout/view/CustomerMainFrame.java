package com.takeout.view;

import com.takeout.controller.*;
import com.takeout.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CustomerMainFrame extends JFrame {
    private User user;
    private JPanel mainPanel;
    private DishController dishController = new DishController();
    private CartController cartController = new CartController();
    private OrderController orderController = new OrderController();
    private UserController userController = new UserController();

    public CustomerMainFrame(User user) {
        this.user = user;
        setTitle("顾客主页 - " + user.getUsername());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 创建导航栏
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new GridLayout(1, 5));

        JButton browseButton = new JButton("餐品浏览");
        JButton cartButton = new JButton("购物车");
        JButton ordersButton = new JButton("我的订单");
        JButton profileButton = new JButton("个人信息");
        JButton logoutButton = new JButton("退出登录");

        navPanel.add(browseButton);
        navPanel.add(cartButton);
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
        browseButton.addActionListener(e -> showDishBrowsePanel());
        cartButton.addActionListener(e -> showCartPanel());
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

    private void showDishBrowsePanel() {
        mainPanel.removeAll();

        // 搜索面板
        JPanel searchPanel = new JPanel();
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("搜索");
        searchPanel.add(new JLabel("搜索餐品:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // 餐品列表
        JPanel dishPanel = new JPanel();
        dishPanel.setLayout(new BoxLayout(dishPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(dishPanel);

        // 获取所有餐品
        List<Dish> dishes = dishController.getAllDishes();
        for (Dish dish : dishes) {
            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setBorder(BorderFactory.createEtchedBorder());

            JLabel nameLabel = new JLabel(dish.getName() + " - ¥" + dish.getPrice());
            JTextArea descArea = new JTextArea(dish.getDescription());
            descArea.setEditable(false);
            descArea.setLineWrap(true);

            JButton addToCartButton = new JButton("加入购物车");
            addToCartButton.addActionListener(e -> {
                Cart cart = new Cart();
                cart.setDish(dish);
                cart.setCustomer(user);
                cart.setMerchant(dish.getMerchant());
                cart.setQuantity(1);
                cartController.addCart(cart);
                JOptionPane.showMessageDialog(this, "已添加到购物车");
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(addToCartButton);

            itemPanel.add(nameLabel, BorderLayout.NORTH);
            itemPanel.add(descArea, BorderLayout.CENTER);
            itemPanel.add(buttonPanel, BorderLayout.SOUTH);

            dishPanel.add(itemPanel);
        }

        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();

        // 搜索功能
        searchButton.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            List<Dish> searchResults = dishController.getAllDishes(); // 这里应该实现搜索逻辑
            // 更新餐品列表显示搜索结果
            dishPanel.removeAll();
            for (Dish dish : searchResults) {
                if (dish.getName().contains(keyword)) {
                    JPanel itemPanel = new JPanel(new BorderLayout());
                    itemPanel.setBorder(BorderFactory.createEtchedBorder());

                    JLabel nameLabel = new JLabel(dish.getName() + " - ¥" + dish.getPrice());
                    JTextArea descArea = new JTextArea(dish.getDescription());
                    descArea.setEditable(false);

                    JButton addToCartButton = new JButton("加入购物车");
                    addToCartButton.addActionListener(ev -> {
                        Cart cart = new Cart();
                        cart.setDish(dish);
                        cart.setCustomer(user);
                        cart.setMerchant(dish.getMerchant());
                        cart.setQuantity(1);
                        cartController.addCart(cart);
                        JOptionPane.showMessageDialog(this, "已添加到购物车");
                    });

                    JPanel buttonPanel = new JPanel();
                    buttonPanel.add(addToCartButton);

                    itemPanel.add(nameLabel, BorderLayout.NORTH);
                    itemPanel.add(descArea, BorderLayout.CENTER);
                    itemPanel.add(buttonPanel, BorderLayout.SOUTH);

                    dishPanel.add(itemPanel);
                }
            }
            dishPanel.revalidate();
            dishPanel.repaint();
        });
    }

    private void showCartPanel() {
        mainPanel.removeAll();
        JPanel cartPanel = new JPanel();
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(cartPanel);

        // 获取当前用户的购物车
        List<Cart> carts = cartController.getCartsByCustomer(user.getId());

        if (carts.isEmpty()) {
            cartPanel.add(new JLabel("购物车为空"));
        } else {
            double total = 0;
            for (Cart cart : carts) {
                JPanel itemPanel = new JPanel(new BorderLayout());
                itemPanel.setBorder(BorderFactory.createEtchedBorder());

                Dish dish = cart.getDish();
                JLabel nameLabel = new JLabel(dish.getName() + " × " + cart.getQuantity() + " - ¥" + (dish.getPrice() * cart.getQuantity()));
                total += dish.getPrice() * cart.getQuantity();

                JButton removeButton = new JButton("删除");
                removeButton.addActionListener(e -> {
                    cartController.deleteCart(cart.getId());
                    showCartPanel(); // 刷新购物车
                });

                JButton checkoutButton = new JButton("结算");
                checkoutButton.addActionListener(e -> {
                    // 创建订单
                    Order order = new Order();
                    order.setCustomer(user);
                    order.setMerchant(dish.getMerchant());
                    order.setDish(dish);
                    order.setQuantity(cart.getQuantity());
                    order.setPrice(dish.getPrice());
                    order.setTotalPrice(dish.getPrice() * cart.getQuantity());
                    order.setStatus(OrderStatus.pending);
                    orderController.addOrder(order);

                    // 从购物车移除
                    cartController.deleteCart(cart.getId());

                    JOptionPane.showMessageDialog(this, "下单成功！");
                    showCartPanel(); // 刷新购物车
                });

                JPanel buttonPanel = new JPanel();
                buttonPanel.add(removeButton);
                buttonPanel.add(checkoutButton);

                itemPanel.add(nameLabel, BorderLayout.CENTER);
                itemPanel.add(buttonPanel, BorderLayout.EAST);

                cartPanel.add(itemPanel);
            }

            JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            totalPanel.add(new JLabel("总计: ¥" + total));
            cartPanel.add(totalPanel);
        }

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showOrdersPanel() {
        mainPanel.removeAll();
        JPanel orderPanel = new JPanel();
        orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(orderPanel);

        // 获取当前用户的订单
        List<Order> orders = orderController.getOrdersByCustomer(user.getId());

        if (orders.isEmpty()) {
            orderPanel.add(new JLabel("暂无订单"));
        } else {
            for (Order order : orders) {
                JPanel itemPanel = new JPanel(new BorderLayout());
                itemPanel.setBorder(BorderFactory.createEtchedBorder());

                Dish dish = order.getDish();
                JLabel infoLabel = new JLabel(dish.getName() + " × " + order.getQuantity() +
                        " - ¥" + order.getTotalPrice() + " - 状态: " + order.getStatus());

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