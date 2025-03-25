package com.takeout.view;

import com.takeout.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MerchantMainFrame extends JFrame {
    private User user;

    public MerchantMainFrame(User user) {
        this.user = user;
        setTitle("商家主页 - " + user.getUsername());
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 创建导航栏
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new GridLayout(1, 3));

        JButton dishesButton = new JButton("菜品管理");
        JButton ordersButton = new JButton("订单查询");
        JButton profileButton = new JButton("个人信息");

        navPanel.add(dishesButton);
        navPanel.add(ordersButton);
        navPanel.add(profileButton);

        // 主内容面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // 默认显示欢迎信息
        JLabel welcomeLabel = new JLabel("欢迎, " + user.getUsername() + "!", SwingConstants.CENTER);
        mainPanel.add(welcomeLabel, BorderLayout.CENTER);

        // 添加导航栏和主内容面板
        add(navPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        // 导航栏按钮事件
        dishesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.removeAll();
                mainPanel.add(new JLabel("菜品管理功能待实现"), BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

        ordersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.removeAll();
                mainPanel.add(new JLabel("订单查询功能待实现"), BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.removeAll();
                mainPanel.add(new JLabel("个人信息功能待实现"), BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });
    }
}