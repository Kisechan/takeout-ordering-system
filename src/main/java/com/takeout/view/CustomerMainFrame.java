package com.takeout.view;

import com.takeout.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerMainFrame extends JFrame {
    private User user;

    public CustomerMainFrame(User user) {
        this.user = user;
        setTitle("顾客主页 - " + user.getUsername());
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 创建导航栏
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new GridLayout(1, 4));

        JButton browseButton = new JButton("餐品浏览");
        JButton cartButton = new JButton("购物车");
        JButton ordersButton = new JButton("订单查询");
        JButton profileButton = new JButton("个人信息");

        navPanel.add(browseButton);
        navPanel.add(cartButton);
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
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.removeAll();
                mainPanel.add(new JLabel("餐品浏览功能待实现"), BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

        cartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.removeAll();
                mainPanel.add(new JLabel("购物车功能待实现"), BorderLayout.CENTER);
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