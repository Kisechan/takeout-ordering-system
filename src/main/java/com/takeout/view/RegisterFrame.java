package com.takeout.view;

import com.takeout.controller.UserController;
import com.takeout.model.User;
import com.takeout.model.Role;

import javax.swing.*;
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
        setTitle("Register");
        setSize(300, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        phoneField = new JTextField(20);
        roleComboBox = new JComboBox<>(Role.values());
        addressField = new JTextField(20);
        registerButton = new JButton("Register");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Role:"));
        panel.add(roleComboBox);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(registerButton);

        add(panel);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String phone = phoneField.getText();
                Role role = (Role) roleComboBox.getSelectedItem();

                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                user.setPhone(phone);
                user.setRole(role);

                userController.register(user);
                JOptionPane.showMessageDialog(RegisterFrame.this, "Registration successful!");
                dispose(); // 关闭注册窗口
            }
        });
    }
}