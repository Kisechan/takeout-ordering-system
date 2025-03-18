package com.takeout.view;

import com.takeout.model.User;

import javax.swing.*;

public class MainFrame extends JFrame {
    public MainFrame(User user) {
        setTitle("Takeout 订单管理系统");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel welcomeLabel = new JLabel("欢迎 " + user.getUsername() + "！");
        panel.add(welcomeLabel);

        JButton viewOrdersButton = new JButton("View Orders");
        panel.add(viewOrdersButton);

        add(panel);
    }
}