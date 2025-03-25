package com.takeout.view;

import com.takeout.controller.UserController;
import com.takeout.model.User;
import com.takeout.model.Role;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField phoneField;
    private JComboBox<Role> roleComboBox;
    private JTextField addressField;
    private JButton registerButton;

    private UserController userController = new UserController();

    public RegisterFrame() {
        setTitle("注册");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.EAST;

        // 用户名标签和输入框
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("用户名:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        usernameField = new JTextField(20);
        panel.add(usernameField, gbc);

        // 密码标签和输入框
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("密码:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        passwordField = new JPasswordField(20);
        panel.add(passwordField, gbc);

        // 电话标签和输入框
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("电话:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        phoneField = new JTextField(20);
        panel.add(phoneField, gbc);

        // 地址标签和输入框
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("地址:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addressField = new JTextField(20);
        panel.add(addressField, gbc);

        // 角色标签和下拉框
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("角色:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        roleComboBox = new JComboBox<>(Role.values());
        panel.add(roleComboBox, gbc);

        // 注册按钮
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        registerButton = new JButton("注册");
        panel.add(registerButton, gbc);

        add(panel);

        // 注册按钮事件
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String phone = phoneField.getText();
                String address = addressField.getText();
                Role role = (Role) roleComboBox.getSelectedItem();

                if (username.isEmpty() || password.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "请填写完整信息！");
                    return;
                }

                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                user.setPhone(phone);
                user.setAddress(address);
                user.setRole(role);

                userController.register(user);
                JOptionPane.showMessageDialog(RegisterFrame.this, "注册成功！");
                dispose(); // 关闭注册窗口
            }
        });
    }
}