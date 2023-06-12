package org.example.pages.LoginPages;

import org.example.HibernateUtil;
import org.example.entity.User;
import org.example.pages.HomePages.ProfilePage;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AuthPage extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    public static String userEmail;
    private ProfilePage profilePage;

    public AuthPage() {
        setTitle("Authorization");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600); // Увеличили высоту окна
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout()); // Используем GridBagLayout для управления компонентами

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(10, 10, 10, 10); // Добавляем отступы между компонентами

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Sign in");
        JButton registerButton = new JButton("Sign up");

        // Установка оранжевого цвета для текста кнопок
        loginButton.setForeground(Color.ORANGE);
        registerButton.setForeground(Color.ORANGE);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = String.valueOf(passwordField.getPassword());

                if (authenticate(email, password)) {
                    userEmail = email;
                    JOptionPane.showMessageDialog(null, "Authorization succeed!");
                    profilePage = new ProfilePage();
                    setContentPane(profilePage);
                    revalidate();
                } else {
                    JOptionPane.showMessageDialog(null, "Wrong email or password!");
                }
            }
        });

        // Превращение кнопки "Зарегистрироваться" в гиперссылку
        registerButton.setBorderPainted(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setFocusPainted(false);
        registerButton.setOpaque(false);
        registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Обработка нажатия на гиперссылку "Зарегистрироваться"
                JOptionPane.showMessageDialog(null, "Registration");
                // Обработка нажатия на гиперссылку "Зарегистрироваться"
                RegistrationPage registrationPage = new RegistrationPage();
                registrationPage.setVisible(true);
                dispose(); // Закрыть текущую страницу авторизации
            }
        });

        // Размещение компонентов с помощью GridBagLayout
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(emailLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        add(emailField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        add(passwordLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        add(passwordField, constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        add(loginButton, constraints);

        constraints.gridx = 2;
        constraints.gridy = 2;
        add(registerButton, constraints);
    }

    private boolean authenticate(String email, String password) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<User> query = session.createQuery("FROM User WHERE email = :email AND uPassword = :password", User.class);
            query.setParameter("email", email);
            query.setParameter("password", password);
            User user = query.uniqueResult();
            return (user != null); // If user is found, authentication is successful
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

}

