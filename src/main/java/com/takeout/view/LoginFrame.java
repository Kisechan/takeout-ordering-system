package com.takeout.view;

import com.takeout.controller.UserController;
import com.takeout.model.User;
import com.takeout.model.Role;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    private UserController userController = new UserController();

    public LoginFrame() {
        setTitle("登录");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // 设置组件之间的间距

        // 用户名标签和输入框
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("用户名:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        usernameField = new JTextField(20);
        panel.add(usernameField, gbc);

        // 密码标签和输入框
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("密码:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        passwordField = new JPasswordField(20);
        panel.add(passwordField, gbc);

        // 登录按钮
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        loginButton = new JButton("登录");
        panel.add(loginButton, gbc);

        // 注册按钮
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        registerButton = new JButton("注册");
        panel.add(registerButton, gbc);

        add(panel);

        // 登录按钮事件
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                User user = userController.login(username, password);
                if (user != null) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "成功登录！");
                    // 根据用户角色打开不同界面
                    if (user.getRole() == Role.customer) {
                        new CustomerMainFrame(user).setVisible(true);
                    } else if (user.getRole() == Role.merchant) {
                        new MerchantMainFrame(user).setVisible(true);
                    }
                    dispose(); // 关闭登录窗口
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "用户名或密码错误！");
                }
            }
        });

        // 注册按钮事件
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterFrame().setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginFrame().setVisible(true);
            }
        });
    }
}