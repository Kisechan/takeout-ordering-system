package com.takeout.view;

import com.takeout.controller.UserController;
import com.takeout.model.User;
import com.takeout.model.Role;
import com.takeout.util.IconUtil;

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
        IconUtil.setWindowIcon(this);
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
        panel.add(new JLabel("用户名*:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        usernameField = new JTextField(20);
        panel.add(usernameField, gbc);

        // 密码标签和输入框
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("密码*:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        passwordField = new JPasswordField(20);
        panel.add(passwordField, gbc);

        // 电话标签和输入框
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("电话*:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        phoneField = new JTextField(20);
        panel.add(phoneField, gbc);

        // 地址标签和输入框
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("地址*:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addressField = new JTextField(20);
        panel.add(addressField, gbc);

        // 角色标签和下拉框
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("角色*:"), gbc);

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
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                String phone = phoneField.getText().trim();
                String address = addressField.getText().trim();
                Role role = (Role) roleComboBox.getSelectedItem();

                // 合法性校验
                if (username.length() < 2) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "用户名不能为空，且至少2个字符！");
                    return;
                }

                if (password.length() < 6) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "密码不能为空，且至少6位！");
                    return;
                }

                if (!phone.matches("^\\d{11}$")) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "电话必须是11位数字！");
                    return;
                }

                if (address.isEmpty()) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "地址不能为空！");
                    return;
                }

                // 如果通过校验，则构造用户对象并提交
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                user.setPhone(phone);
                user.setAddress(address);
                user.setRole(role);

                userController.register(user);
                JOptionPane.showMessageDialog(RegisterFrame.this, "注册成功！");
                dispose();
            }
        });

    }
}