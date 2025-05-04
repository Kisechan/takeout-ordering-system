package com.takeout.view;

import com.takeout.controller.*;
import com.takeout.model.*;
import com.takeout.util.IconUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.List;

public class CustomerMainFrame extends JFrame {
    private User user;
    private JPanel mainPanel;
    private DishController dishController = new DishController();
    private CartController cartController = new CartController();
    private OrderController orderController = new OrderController();
    private UserController userController = new UserController();

    // 样式常量
    private static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private static final Color SECONDARY_COLOR = new Color(220, 220, 220);
    private static final Font TITLE_FONT = new Font("微软雅黑", Font.BOLD, 16);
    private static final Font NORMAL_FONT = new Font("微软雅黑", Font.PLAIN, 14);
    private static final int CARD_WIDTH = 280;
    private static final int CARD_HEIGHT = 220;

    // 修改构造函数，设置全局样式
    public CustomerMainFrame(User user) {
        IconUtil.setWindowIcon(this);
        this.user = user;
        setTitle("顾客主页 - " + user.getUsername());
        setSize(900, 700);  // 稍微增大窗口尺寸
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 创建导航栏 - 改进样式
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new GridLayout(1, 5, 5, 5));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        navPanel.setBackground(PRIMARY_COLOR);

        JButton browseButton = createStyledButton("餐品浏览");
        JButton cartButton = createStyledButton("购物车");
        JButton ordersButton = createStyledButton("我的订单");
        JButton profileButton = createStyledButton("个人信息");
        JButton logoutButton = createStyledButton("退出登录");

        navPanel.add(browseButton);
        navPanel.add(cartButton);
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
        browseButton.addActionListener(e -> showDishBrowsePanel());
        cartButton.addActionListener(e -> showCartPanel());
        ordersButton.addActionListener(e -> showOrdersPanel());
        profileButton.addActionListener(e -> showProfilePanel());
        logoutButton.addActionListener(e -> logout());
    }

    // 创建统一风格的按钮
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

    // 创建统一风格的卡片面板
    private JPanel createCardPanel() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SECONDARY_COLOR, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        return card;
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
        mainPanel.setLayout(new BorderLayout());

        // 搜索面板 - 改进样式
        JPanel searchPanel = new JPanel();
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        JTextField searchField = new JTextField(20);
        searchField.setFont(NORMAL_FONT);
        JButton searchButton = createStyledButton("搜索");
        searchPanel.add(new JLabel("搜索餐品:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // 餐品列表 - 使用瀑布流布局
        JPanel dishPanel = new JPanel();
        dishPanel.setLayout(new WrapLayout(FlowLayout.LEFT, 15, 15));
        dishPanel.setBackground(new Color(245, 245, 245));
        JScrollPane scrollPane = new JScrollPane(dishPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // 获取所有餐品
        List<Dish> dishes = dishController.getAllDishes();
        for (Dish dish : dishes) {
            JPanel card = createCardPanel();
            // 信息区域
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setBackground(Color.WHITE);

            JLabel nameLabel = new JLabel(dish.getName());
            nameLabel.setFont(TITLE_FONT);
            nameLabel.setForeground(PRIMARY_COLOR);

            JLabel priceLabel = new JLabel("¥" + dish.getPrice());
            priceLabel.setFont(NORMAL_FONT);

            JTextArea descArea = new JTextArea(dish.getDescription());
            descArea.setEditable(false);
            descArea.setLineWrap(true);
            descArea.setWrapStyleWord(true);
            descArea.setBackground(Color.WHITE);
            descArea.setFont(NORMAL_FONT);
            descArea.setRows(2);

            infoPanel.add(nameLabel);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            infoPanel.add(priceLabel);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            infoPanel.add(descArea);

            // 按钮区域
            JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

            // 加入购物车按钮 - 带数量选择
            JButton addToCartButton = createStyledButton("加入购物车");
            addToCartButton.addActionListener(e -> {
                JPanel panel = new JPanel(new BorderLayout(5, 5));
                panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                JLabel label = new JLabel("选择数量:");
                label.setFont(NORMAL_FONT);

                JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
                JSpinner.NumberEditor editor = new JSpinner.NumberEditor(quantitySpinner, "#");
                quantitySpinner.setEditor(editor);
                quantitySpinner.setFont(NORMAL_FONT);

                panel.add(label, BorderLayout.NORTH);
                panel.add(quantitySpinner, BorderLayout.CENTER);

                int result = JOptionPane.showConfirmDialog(this, panel, "加入购物车",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    int quantity = (Integer) quantitySpinner.getValue();
                    Cart cart = new Cart();
                    cart.setDish(dish);
                    cart.setCustomer(user);
                    cart.setMerchant(dish.getMerchant());
                    cart.setQuantity(quantity);
                    cart.setUpdateTime(LocalDateTime.now());
                    cartController.addCart(cart);
                    JOptionPane.showMessageDialog(this, "已添加到购物车", "成功", JOptionPane.INFORMATION_MESSAGE);
                }
            });

            // 立即下单按钮
            JButton orderNowButton = createStyledButton("立即下单");
            orderNowButton.addActionListener(e -> {
                JPanel panel = new JPanel(new BorderLayout(5, 5));
                panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                JLabel label = new JLabel("选择数量:");
                label.setFont(NORMAL_FONT);

                JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
                JSpinner.NumberEditor editor = new JSpinner.NumberEditor(quantitySpinner, "#");
                quantitySpinner.setEditor(editor);
                quantitySpinner.setFont(NORMAL_FONT);

                panel.add(label, BorderLayout.NORTH);
                panel.add(quantitySpinner, BorderLayout.CENTER);

                int result = JOptionPane.showConfirmDialog(this, panel, "立即下单",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    int quantity = (Integer) quantitySpinner.getValue();

                    // 创建订单
                    Order order = new Order();
                    order.setCustomer(user);
                    order.setMerchant(dish.getMerchant());
                    order.setDish(dish);
                    order.setQuantity(quantity);
                    order.setPrice(dish.getPrice());
                    order.setTotalPrice(dish.getPrice() * quantity);
                    order.setStatus(OrderStatus.paid);
                    order.setAddTime(LocalDateTime.now());
                    orderController.addOrder(order);

                    JOptionPane.showMessageDialog(this, "下单成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                }
            });

            buttonPanel.add(orderNowButton);
            buttonPanel.add(addToCartButton);

            card.add(infoPanel, BorderLayout.CENTER);
            card.add(buttonPanel, BorderLayout.SOUTH);

            dishPanel.add(card);
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
        mainPanel.setLayout(new BorderLayout());

        // 创建主内容面板
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 创建卡片容器面板 - 使用WrapLayout实现瀑布流
        JPanel cardsPanel = new JPanel(new WrapLayout(FlowLayout.LEFT, 15, 15));
        cardsPanel.setBackground(new Color(245, 245, 245));

        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // 获取当前用户的购物车
        List<Cart> carts = cartController.getCartsByCustomer(user.getId());

        if (carts.isEmpty()) {
            JLabel emptyLabel = new JLabel("购物车为空", SwingConstants.CENTER);
            emptyLabel.setFont(TITLE_FONT);
            emptyLabel.setForeground(PRIMARY_COLOR);
            cardsPanel.add(emptyLabel);
        } else {
            double total = 0;

            for (Cart cart : carts) {
                Dish dish = cart.getDish();
                double itemTotal = dish.getPrice() * cart.getQuantity();
                total += itemTotal;

                // 创建卡片
                JPanel card = createCardPanel();
                card.setPreferredSize(new Dimension(CARD_WIDTH, 200));

                // 卡片内容布局
                JPanel cardContent = new JPanel(new BorderLayout(10, 10));
                cardContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                cardContent.setBackground(Color.WHITE);

                // 商品信息区域
                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                infoPanel.setBackground(Color.WHITE);

                JLabel nameLabel = new JLabel(dish.getName());
                nameLabel.setFont(TITLE_FONT);
                nameLabel.setForeground(PRIMARY_COLOR);

                JLabel priceLabel = new JLabel("单价: ¥" + dish.getPrice());
                priceLabel.setFont(NORMAL_FONT);

                // 数量调整面板
                JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                quantityPanel.setBackground(Color.WHITE);

                JButton decreaseButton = createStyledButton("-");
                JLabel quantityLabel = new JLabel("数量: " + cart.getQuantity());
                quantityLabel.setFont(NORMAL_FONT);
                JButton increaseButton = createStyledButton("+");

                decreaseButton.addActionListener(e -> updateCartQuantity(cart, cart.getQuantity() - 1));
                increaseButton.addActionListener(e -> updateCartQuantity(cart, cart.getQuantity() + 1));

                quantityPanel.add(decreaseButton);
                quantityPanel.add(quantityLabel);
                quantityPanel.add(increaseButton);

                JLabel totalLabel = new JLabel("小计: ¥" + String.format("%.2f", itemTotal));
                totalLabel.setFont(NORMAL_FONT);

                infoPanel.add(nameLabel);
                infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                infoPanel.add(priceLabel);
                infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                infoPanel.add(quantityPanel);
                infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                infoPanel.add(totalLabel);

                // 操作按钮区域 - 统一放在卡片底部
                JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
                buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

                JButton removeButton = createStyledButton("删除");
                removeButton.addActionListener(e -> {
                    cartController.deleteCart(cart.getId());
                    showCartPanel(); // 刷新
                });

                JButton checkoutButton = createStyledButton("结算");
                checkoutButton.addActionListener(e -> checkoutCartItem(cart));

                buttonPanel.add(removeButton);
                buttonPanel.add(checkoutButton);

                cardContent.add(infoPanel, BorderLayout.CENTER);
                cardContent.add(buttonPanel, BorderLayout.SOUTH);

                card.add(cardContent);
                cardsPanel.add(card);
            }

            // 总计面板
            JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            totalPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            totalPanel.setBackground(new Color(245, 245, 245));

            JLabel totalLabel = new JLabel("总计: ¥" + String.format("%.2f", total));
            totalLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
            totalLabel.setForeground(PRIMARY_COLOR);

            JButton checkoutAllButton = createStyledButton("一键结算");
            checkoutAllButton.addActionListener(e -> checkoutAllItems(carts));

            totalPanel.add(totalLabel);
            totalPanel.add(Box.createRigidArea(new Dimension(20, 0)));
            totalPanel.add(checkoutAllButton);

            contentPanel.add(scrollPane, BorderLayout.CENTER);
            contentPanel.add(totalPanel, BorderLayout.SOUTH);
        }

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // 提取的公共方法 - 更新购物车数量
    private void updateCartQuantity(Cart cart, int newQuantity) {
        if (newQuantity > 0) {
            cart.setQuantity(newQuantity);
            cartController.updateCart(cart);
        } else {
            cartController.deleteCart(cart.getId());
        }
        showCartPanel(); // 刷新
    }

    // 提取的公共方法 - 结算单个商品
    private void checkoutCartItem(Cart cart) {
        Dish dish = cart.getDish();

        Order order = new Order();
        order.setCustomer(user);
        order.setMerchant(dish.getMerchant());
        order.setDish(dish);
        order.setQuantity(cart.getQuantity());
        order.setPrice(dish.getPrice());
        order.setTotalPrice(dish.getPrice() * cart.getQuantity());
        order.setStatus(OrderStatus.paid);
        order.setAddTime(LocalDateTime.now());
        orderController.addOrder(order);

        cartController.deleteCart(cart.getId());
        JOptionPane.showMessageDialog(this, "下单成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
        showCartPanel();
    }

    // 提取的公共方法 - 一键结算所有商品
    private void checkoutAllItems(List<Cart> carts) {
        for (Cart cart : carts) {
            checkoutCartItem(cart);
        }
    }

    private void showOrdersPanel() {
        mainPanel.removeAll();
        JPanel orderPanel = new JPanel();
        orderPanel.setLayout(new WrapLayout(FlowLayout.LEFT, 15, 15));
        orderPanel.setBackground(new Color(245, 245, 245));

        JScrollPane scrollPane = new JScrollPane(orderPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // 获取当前用户的订单
        List<Order> orders = orderController.getOrdersByCustomer(user.getId());

        if (orders.isEmpty()) {
            JLabel emptyLabel = new JLabel("暂无订单", SwingConstants.CENTER);
            emptyLabel.setFont(TITLE_FONT);
            emptyLabel.setForeground(PRIMARY_COLOR);
            orderPanel.add(emptyLabel);
        } else {
            for (Order order : orders) {
                JPanel card = createCardPanel();
                card.setPreferredSize(new Dimension(CARD_WIDTH, 150));

                Dish dish = order.getDish();

                JLabel nameLabel = new JLabel(dish.getName());
                nameLabel.setFont(TITLE_FONT);
                nameLabel.setForeground(PRIMARY_COLOR);

                JLabel quantityLabel = new JLabel("数量: " + order.getQuantity());
                quantityLabel.setFont(NORMAL_FONT);

                JLabel priceLabel = new JLabel("总价: ¥" + order.getTotalPrice());
                priceLabel.setFont(NORMAL_FONT);

                JLabel statusLabel = new JLabel("状态: " + StatusMap.translate(order.getStatus().name()));
                statusLabel.setFont(NORMAL_FONT);

                JLabel timeLabel = new JLabel("时间: " + order.getAddTime().toLocalDate());
                timeLabel.setFont(NORMAL_FONT);

                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                infoPanel.setBackground(Color.WHITE);

                infoPanel.add(nameLabel);
                infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                infoPanel.add(quantityLabel);
                infoPanel.add(priceLabel);
                infoPanel.add(statusLabel);
                infoPanel.add(timeLabel);

                card.add(infoPanel, BorderLayout.CENTER);
                orderPanel.add(card);
            }
        }

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // 个人信息面板
    private void showProfilePanel() {
        mainPanel.removeAll();
        JPanel profilePanel = new JPanel(new GridBagLayout());
        profilePanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.EAST;

        // 获取最新用户信息
        User currentUser = userController.getUserById(user.getId());

        JLabel usernameLabel = new JLabel("用户名:");
        usernameLabel.setFont(NORMAL_FONT);
        JTextField usernameField = new JTextField(currentUser.getUsername(), 20);
        usernameField.setFont(NORMAL_FONT);
        usernameField.setEditable(false);

        JLabel phoneLabel = new JLabel("电话:");
        phoneLabel.setFont(NORMAL_FONT);
        JTextField phoneField = new JTextField(currentUser.getPhone(), 20);
        phoneField.setFont(NORMAL_FONT);
        // 电话格式验证
        phoneField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String text = ((JTextField) input).getText();
                return text.matches("\\d{11}"); // 简单验证11位数字
            }
        });

        JLabel addressLabel = new JLabel("地址:");
        addressLabel.setFont(NORMAL_FONT);
        JTextField addressField = new JTextField(currentUser.getAddress(), 20);
        addressField.setFont(NORMAL_FONT);

        JButton saveButton = createStyledButton("保存修改");
        saveButton.addActionListener(e -> {
            if (!phoneField.getInputVerifier().verify(phoneField)) {
                JOptionPane.showMessageDialog(this, "请输入有效的11位手机号码", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            currentUser.setPhone(phoneField.getText());
            currentUser.setAddress(addressField.getText());
            userController.updateUser(currentUser);
            JOptionPane.showMessageDialog(this, "信息更新成功", "成功", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton changePassButton = createStyledButton("修改密码");
        changePassButton.addActionListener(e -> {
            JPasswordField oldPassField = new JPasswordField();
            JPasswordField newPassField = new JPasswordField();
            JPasswordField confirmPassField = new JPasswordField();

            JPanel passPanel = new JPanel(new GridLayout(3, 2, 5, 5));
            passPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel oldPassLabel = new JLabel("旧密码:");
            oldPassLabel.setFont(NORMAL_FONT);
            passPanel.add(oldPassLabel);
            passPanel.add(oldPassField);

            JLabel newPassLabel = new JLabel("新密码:");
            newPassLabel.setFont(NORMAL_FONT);
            passPanel.add(newPassLabel);
            passPanel.add(newPassField);

            JLabel confirmPassLabel = new JLabel("确认密码:");
            confirmPassLabel.setFont(NORMAL_FONT);
            passPanel.add(confirmPassLabel);
            passPanel.add(confirmPassField);

            int result = JOptionPane.showConfirmDialog(this, passPanel, "修改密码",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String oldPass = new String(oldPassField.getPassword());
                String newPass = new String(newPassField.getPassword());
                String confirmPass = new String(confirmPassField.getPassword());

                if (!oldPass.equals(currentUser.getPassword())) {
                    JOptionPane.showMessageDialog(this, "旧密码错误", "错误", JOptionPane.ERROR_MESSAGE);
                } else if (!newPass.equals(confirmPass)) {
                    JOptionPane.showMessageDialog(this, "两次输入的新密码不一致", "错误", JOptionPane.ERROR_MESSAGE);
                } else if (newPass.length() < 6) {
                    JOptionPane.showMessageDialog(this, "密码长度不能少于6位", "错误", JOptionPane.ERROR_MESSAGE);
                } else {
                    currentUser.setPassword(newPass);
                    userController.updateUser(currentUser);
                    JOptionPane.showMessageDialog(this, "密码修改成功", "成功", JOptionPane.INFORMATION_MESSAGE);
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